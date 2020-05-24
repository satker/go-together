package org.go.together.logic.repository.utils.sql;

import org.apache.commons.lang3.StringUtils;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.*;

import static org.go.together.logic.repository.utils.sql.ObjectStringParser.parseToString;

public class CustomSqlBuilder<E extends IdentifiedEntity> {
    private final StringBuilder query;
    private final EntityManager entityManager;
    private final Class<E> clazz;
    private final Map<String, String> joinTables;

    public CustomSqlBuilder(Class<E> clazz, EntityManager entityManager, String selectRow) {
        joinTables = new HashMap<>();
        String entityLink = getEntityLink(clazz);
        String selectQuery = selectRow == null ? entityLink : entityLink + "." + selectRow;
        query = new StringBuilder("select distinct " + selectQuery + " FROM " + clazz.getSimpleName() + " " + entityLink);
        appendJoins(clazz, query);
        this.entityManager = entityManager;
        this.clazz = clazz;
    }

    private void appendJoins(Class<E> clazz, StringBuilder query) {
        Arrays.stream(clazz.getDeclaredFields())
                .filter(field1 -> field1.getAnnotation(ElementCollection.class) != null || field1.getAnnotation(ManyToMany.class) != null)
                .forEach(field1 -> {
                    String generatedTableName = getJoinTableName(field1, clazz);
                    query.append(" left join ")
                            .append(getEntityField(field1.getName(), clazz))
                            .append(" ")
                            .append(generatedTableName);
                    joinTables.put(field1.getName(), generatedTableName);
                });
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

    public CustomSqlBuilder<E> where(WhereBuilder whereBuilder) {
        query.append(whereBuilder.getWhereQuery());
        return this;
    }

    public Collection<E> fetchAll() {
        return entityManager.createQuery(query.toString(), clazz).getResultList();
    }

    public Optional<E> fetchOne() {
        return entityManager.createQuery(query.toString(), clazz).getResultStream().findFirst();
    }

    public Collection<E> fetchWithPageable(int start, int pageSize) {
        TypedQuery<E> query = entityManager.createQuery(this.query.toString(), clazz);
        query.setFirstResult(start);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Collection<Object> fetchWithPageableNotDefined(int start, int pageSize) {
        Query query = entityManager.createQuery(this.query.toString());
        query.setFirstResult(start);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Number getCountRows() {
        return entityManager.createQuery("SELECT COUNT (DISTINCT " + getEntityLink(clazz) + ".id) FROM " +
                clazz.getSimpleName() + " " + getEntityLink(clazz), Number.class)
                .getSingleResult();
    }

    public Number getCountRowsWhere(WhereBuilder whereBuilder) {
        StringBuilder query = new StringBuilder("SELECT COUNT (DISTINCT " + getEntityLink(clazz) + ".id) FROM " +
                clazz.getSimpleName() + " " + getEntityLink(clazz) + " ");
        appendJoins(clazz, query);
        query.append(whereBuilder.getWhereQuery());
        return entityManager.createQuery(query.toString(), Number.class)
                .getSingleResult();
    }

    public class WhereBuilder {
        private final StringBuilder whereQuery;

        private static final String AND = " and ";
        private static final String OR = " or ";

        public WhereBuilder(Boolean isGroup) {
            whereQuery = new StringBuilder(isGroup ? StringUtils.EMPTY : " WHERE ");
        }

        protected StringBuilder getWhereQuery() {
            return whereQuery;
        }

        public WhereBuilder condition(String field, SqlOperator sqlOperator, Object value) {
            String parsedValue = parseToString(value);
            Optional<Map.Entry<String, String>> joinTableNameOptional = joinTables.entrySet().stream()
                    .filter(joinName -> field.startsWith(joinName.getKey() + "."))
                    .findFirst();
            String fieldName = getEntityField(field, clazz);
            if (joinTableNameOptional.isPresent()) {
                Map.Entry<String, String> joinTableName = joinTableNameOptional.get();
                fieldName = field.replaceFirst(joinTableName.getKey(), joinTableName.getValue());
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
            whereQuery.append(AND);
            return this;
        }

        public WhereBuilder or() {
            whereQuery.append(OR);
            return this;
        }

        public WhereBuilder cutLastAnd() {
            whereQuery.setLength(whereQuery.length() - AND.length());
            return this;
        }
    }
}
