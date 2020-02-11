package org.go.together.logic.repository.utils.sql;

import org.apache.commons.lang3.StringUtils;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.go.together.logic.repository.utils.sql.ObjectStringParser.parseToString;

public class CustomSqlBuilder<E extends IdentifiedEntity> {
    private final StringBuilder query;
    private final EntityManager entityManager;
    private final Class<E> clazz;

    public CustomSqlBuilder(Class<E> clazz, EntityManager entityManager) {
        query = new StringBuilder("FROM " + clazz.getSimpleName());
        this.entityManager = entityManager;
        this.clazz = clazz;
    }

    public CustomSqlBuilder<E> groupingByLastRow(String groupingField, String fieldDate, WhereBuilder whereBuilder) {
        String fields = Stream.of(clazz.getFields()).map(Field::getName).collect(Collectors.joining(", "));
        query.append("(select ")
                .append(fields)
                .append(", row_number() over (partition by ")
                .append(groupingField)
                .append(" order by ")
                .append(fieldDate)
                .append(" desc) as rn")
                .append(query)
                .append(whereBuilder.getWhereQuery())
                .append(") t")
                .append(" WHERE rn = 1");
        return orderBy(groupingField, false);
    }

    public CustomSqlBuilder<E> orderBy(String field, Boolean isUnique) {
        query.append(" ORDER BY ")
                .append(field)
                .append(isUnique ? " DESC " : StringUtils.EMPTY);
        return this;
    }

    public CustomSqlBuilder<E> where(WhereBuilder whereBuilder) {
        query.append(whereBuilder.getWhereQuery());
        return this;
    }

    @Transactional
    public Collection<E> fetchAll() {
        return entityManager.createQuery(query.toString(), clazz).getResultList();
    }

    @Transactional
    public Optional<E> fetchOne() {
        return entityManager.createQuery(query.toString(), clazz).getResultStream().findFirst();
    }

    public static class WhereBuilder {
        private StringBuilder whereQuery;

        public WhereBuilder() {
            whereQuery = new StringBuilder(" WHERE ");
        }

        protected StringBuilder getWhereQuery() {
            return whereQuery;
        }

        public WhereBuilder condition(String field, SqlOperator sqlOperator, Object value) {
            String parsedValue = parseToString(value);
            whereQuery.append(sqlOperator.getBiFunction().apply(field, parsedValue));
            return this;
        }

        public WhereBuilder group(WhereBuilder whereBuilder) {
            whereQuery.append("(")
                    .append(whereBuilder.getWhereQuery())
                    .append(")");
            return this;
        }

        public WhereBuilder and() {
            whereQuery.append(" and ");
            return this;
        }

        public WhereBuilder or() {
            whereQuery.append(" or ");
            return this;
        }
    }
}
