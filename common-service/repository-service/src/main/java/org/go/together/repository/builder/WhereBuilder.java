package org.go.together.repository.builder;

import org.apache.commons.lang3.StringUtils;
import org.go.together.repository.builder.dto.WhereDto;
import org.go.together.repository.entities.IdentifiedEntity;
import org.go.together.repository.sql.SqlOperator;

import java.util.Map;

import static org.go.together.repository.sql.ObjectStringParser.parseToString;

public class WhereBuilder<E extends IdentifiedEntity> {
    private static final String AND = " and ";
    private static final String OR = " or ";
    private final Class<E> clazz;
    private StringBuilder join;
    private StringBuilder whereQuery;
    private JoinBuilder<E> joinBuilder;

    public WhereBuilder(Class<E> clazz) {
        this.clazz = clazz;
    }

    public WhereBuilder<E> builder(Boolean isGroup) {
        this.joinBuilder = new JoinBuilder<>(clazz).builder();
        this.join = new StringBuilder();
        this.whereQuery = new StringBuilder(isGroup ? StringUtils.EMPTY : " WHERE ");
        return this;
    }

    public WhereDto build() {
        return WhereDto.builder().join(join).whereQuery(whereQuery).build();
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
        return joinBuilder.getFieldWithJoin(field, this::enrichJoin);
    }

    private void enrichJoin(Map.Entry<String, String> joinTableName) {
        String leftJoin = joinBuilder.createLeftJoin(joinTableName);
        if (!this.join.toString().contains(leftJoin)) {
            this.join.append(leftJoin);
        }
    }

    public WhereBuilder<E> group(WhereBuilder<E> whereBuilder) {
        whereQuery.append("(")
                .append(whereBuilder.build().getWhereQuery())
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
