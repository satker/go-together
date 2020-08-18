package org.go.together.repository.builder;

import org.apache.commons.lang3.StringUtils;
import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.repository.sql.SqlOperator;

import java.util.Map;
import java.util.Optional;

import static org.go.together.repository.builder.utils.BuilderUtils.getEntityField;
import static org.go.together.repository.sql.ObjectStringParser.parseToString;

public class WhereBuilder<E extends IdentifiedEntity> {
    private static final String AND = " and ";
    private static final String OR = " or ";
    private final StringBuilder join;
    private final StringBuilder whereQuery;
    private final JoinBuilder<E> joinBuilder;
    private final Class<E> clazz;

    public WhereBuilder(Boolean isGroup, Class<E> clazz) {
        this.clazz = clazz;
        joinBuilder = new JoinBuilder<>(clazz);
        join = new StringBuilder();
        whereQuery = new StringBuilder(isGroup ? StringUtils.EMPTY : " WHERE ");
    }

    public StringBuilder getWhereQuery() {
        return whereQuery;
    }

    public StringBuilder getJoinQuery() {
        return join;
    }

    public void addJoin(StringBuilder join) {
        this.join.append(join);
    }

    public WhereBuilder<E> condition(String field, SqlOperator sqlOperator, Object value) {
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
            String leftJoin = joinBuilder.createLeftJoin(joinTableName);
            if (!join.toString().contains(leftJoin)) {
                join.append(leftJoin);
            }
            fieldName = field.replaceFirst(joinTableName.getKey(), joinTableName.getValue());
        }
        return fieldName;
    }

    public WhereBuilder<E> group(WhereBuilder<E> whereBuilder) {
        whereQuery.append("(")
                .append(whereBuilder.getWhereQuery())
                .append(")");
        return this;
    }

    public WhereBuilder<E> and() {
        whereQuery.append(AND);
        return this;
    }

    public WhereBuilder<E> or() {
        whereQuery.append(OR);
        return this;
    }

    public void cutLastAnd() {
        if (whereQuery.toString().endsWith(AND)) {
            whereQuery.setLength(whereQuery.length() - AND.length());
        }
    }

    public void cutLastOr() {
        if (whereQuery.toString().endsWith(OR)) {
            whereQuery.setLength(whereQuery.length() - OR.length());
        }
    }
}
