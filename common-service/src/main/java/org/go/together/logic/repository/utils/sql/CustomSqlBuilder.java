package org.go.together.logic.repository.utils.sql;

import org.apache.commons.lang3.StringUtils;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.ElementCollection;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.go.together.logic.repository.utils.sql.ObjectStringParser.parseToString;

public class CustomSqlBuilder<E extends IdentifiedEntity> {
    private final StringBuilder query;
    private final EntityManager entityManager;
    private final Class<E> clazz;
    private final Map<String, String> joinTables;

    public CustomSqlBuilder(Class<E> clazz, EntityManager entityManager) {
        joinTables = new HashMap<>();
        String entityLink = getEntityLink(clazz);
        query = new StringBuilder("FROM " + clazz.getSimpleName() + " " + entityLink);
        Arrays.stream(clazz.getDeclaredFields())
                .filter(field1 -> field1.getAnnotation(ElementCollection.class) != null)
                .forEach(field1 -> {
                    String generatedTableName = getJoinTableName(field1, clazz);
                    query.append(" left join ")
                            .append(getEntityField(field1.getName(), clazz))
                            .append(" ")
                            .append(generatedTableName);
                    joinTables.put(field1.getName(), generatedTableName);
                });
        this.entityManager = entityManager;
        this.clazz = clazz;
    }

    private String getJoinTableName(Field field, Class<E> clazz) {
        return getEntityLink(clazz) + "_" + field.getName();
    }

    private String getEntityLink(Class<E> clazz) {
        char[] chars = clazz.getSimpleName().toCharArray();
        StringBuilder entityLink = new StringBuilder();
        for (char aChar : chars) {
            if (Character.isUpperCase(aChar)) {
                entityLink.append(aChar);
            }
        }
        return entityLink.toString().toLowerCase();
    }

    private String getEntityField(String field, Class<E> clazz) {
        return getEntityLink(clazz) + "." + field;
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

    @Transactional
    public Collection<E> fetchAllPageable(int start, int end) {
        TypedQuery<E> query = entityManager.createQuery(this.query.toString(), clazz);
        query.setFirstResult(start);
        query.setMaxResults(end);
        return query.getResultList();
    }

    public class WhereBuilder {
        private StringBuilder whereQuery;
        private Class<E> clazz;

        public WhereBuilder(Class<E> clazz, Boolean isGroup) {
            this.clazz = clazz;
            whereQuery = new StringBuilder(isGroup ? StringUtils.EMPTY : " WHERE ");
        }

        protected StringBuilder getWhereQuery() {
            return whereQuery;
        }

        public WhereBuilder condition(String field, SqlOperator sqlOperator, Object value) {
            String parsedValue = parseToString(value);
            String joinTableName = joinTables.get(field);
            String fieldName = getEntityField(field, clazz);
            if (StringUtils.isNotBlank(joinTableName)) {
                fieldName = joinTableName;
            }
            whereQuery.append(sqlOperator.getBiFunction().apply(fieldName, parsedValue));
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