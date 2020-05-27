package org.go.together.logic.repository.builder;

import org.apache.commons.lang3.StringUtils;
import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.logic.repository.utils.sql.SqlOperator;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static org.go.together.logic.repository.builder.utils.BuilderUtils.getEntityField;
import static org.go.together.logic.repository.builder.utils.BuilderUtils.getEntityLink;
import static org.go.together.logic.repository.utils.sql.ObjectStringParser.parseToString;

public class SqlBuilder<E extends IdentifiedEntity> {
    private final String from;
    private final StringBuilder query;
    private final EntityManager entityManager;
    private final Class<E> clazz;
    private final JoinBuilder<E> joinBuilder;
    private final String select;

    public SqlBuilder(Class<E> clazz, EntityManager entityManager, String selectRow) {
        String entityLink = getEntityLink(clazz);
        String selectQuery = selectRow == null ? entityLink : entityLink + "." + selectRow;
        select = "select distinct " + selectQuery;
        from = " FROM " + clazz.getSimpleName() + " " + entityLink;
        joinBuilder = new JoinBuilder<>(clazz);
        query = new StringBuilder();
        this.entityManager = entityManager;
        this.clazz = clazz;
    }

    public SqlBuilder<E> where(WhereBuilder whereBuilder) {
        String joinQuery = whereBuilder.getJoinQuery().toString();
        if (StringUtils.isNotEmpty(joinQuery)) {
            query.append(select);
            query.append(from);
            query.append(joinQuery);
        } else {
            query.append(from);
        }
        query.append(whereBuilder.getWhereQuery());
        return this;
    }

    public Collection<E> fetchAll() {
        return entityManager.createQuery(getQuery(), clazz).getResultList();
    }

    public Optional<E> fetchOne() {
        return entityManager.createQuery(getQuery(), clazz).getResultStream().findFirst();
    }

    public Collection<E> fetchWithPageable(int start, int pageSize) {
        TypedQuery<E> query = entityManager.createQuery(getQuery(), clazz);
        query.setFirstResult(start);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Collection<Object> fetchWithPageableNotDefined(int start, int pageSize) {
        Query query = entityManager.createQuery(getQuery());
        query.setFirstResult(start);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Collection<Object> fetchAllNotDefined() {
        return entityManager.createQuery(getQuery()).getResultList();
    }

    public String getQuery() {
        if (StringUtils.isNotEmpty(query)) {
            return query.toString();
        } else {
            return from;
        }
    }

    public Number getCountRows() {
        return entityManager.createQuery("SELECT COUNT (DISTINCT " + getEntityLink(clazz) + ".id) FROM " +
                clazz.getSimpleName() + " " + getEntityLink(clazz), Number.class)
                .getSingleResult();
    }

    public Number getCountRowsWhere(WhereBuilder whereBuilder) {
        String query = "SELECT COUNT (DISTINCT " + getEntityLink(clazz) + ".id) FROM " +
                clazz.getSimpleName() + " " + getEntityLink(clazz) + " " + whereBuilder.getJoinQuery() +
                whereBuilder.getWhereQuery();
        return entityManager.createQuery(query, Number.class).getSingleResult();
    }

    public class WhereBuilder {
        private final StringBuilder join;
        private final StringBuilder whereQuery;

        private static final String AND = " and ";
        private static final String OR = " or ";

        public WhereBuilder(Boolean isGroup) {
            join = new StringBuilder();
            whereQuery = new StringBuilder(isGroup ? StringUtils.EMPTY : " WHERE ");
        }

        protected StringBuilder getWhereQuery() {
            return whereQuery;
        }

        protected StringBuilder getJoinQuery() {
            return join;
        }

        public WhereBuilder condition(String field, SqlOperator sqlOperator, Object value) {
            String parsedValue = parseToString(value);
            String fieldName = getFieldWithJoin(field);
            whereQuery.append(sqlOperator.getBiFunction().apply(fieldName, parsedValue));
            return this;
        }

        private String getFieldWithJoin(String field) {
            String fieldName = getEntityField(field, clazz);
            Optional<Map.Entry<String, String>> joinTableNameOptional = joinBuilder.getJoinTables().entrySet().stream()
                    .filter(joinName -> field.startsWith(joinName.getKey()))
                    .findFirst();
            if (joinTableNameOptional.isPresent()) {
                Map.Entry<String, String> joinTableName = joinTableNameOptional.get();
                join.append(joinBuilder.createLeftJoin(joinTableName));
                fieldName = field.replaceFirst(joinTableName.getKey(), joinTableName.getValue());
            }
            return fieldName;
        }

        public WhereBuilder group(WhereBuilder whereBuilder) {
            whereQuery.append("(")
                    .append(whereBuilder.getWhereQuery())
                    .append(")");
            return this;
        }

        public WhereBuilder and() {
            whereQuery.append(AND);
            return this;
        }

        public WhereBuilder or() {
            whereQuery.append(OR);
            return this;
        }

        public void cutLastAnd() {
            whereQuery.setLength(whereQuery.length() - AND.length());
        }
    }
}
