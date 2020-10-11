package org.go.together.repository.builder;

import org.apache.commons.lang3.StringUtils;
import org.go.together.repository.entities.IdentifiedEntity;
import org.go.together.repository.sql.SqlOperator;

import java.util.Map;

import static org.go.together.repository.builder.utils.BuilderUtils.createLeftJoin;
import static org.go.together.repository.sql.ObjectStringParser.parseToString;

public class Where {
    private final StringBuilder join;
    private final StringBuilder whereQuery;

    private Where(StringBuilder join, StringBuilder whereQuery) {
        this.join = join;
        this.whereQuery = whereQuery;
    }

    public static <E extends IdentifiedEntity> WhereBuilder<E> builder(Class<E> clazz, Boolean isGroup) {
        return new WhereBuilder<E>(isGroup, clazz);
    }

    public StringBuilder getWhereQuery() {
        return whereQuery;
    }

    public StringBuilder getJoin() {
        return join;
    }

    public static class WhereBuilder<E extends IdentifiedEntity> {
        private static final String AND = " and ";
        private static final String OR = " or ";

        private final Class<E> clazz;
        private final StringBuilder join;
        private final StringBuilder whereQuery;
        private final JoinBuilder<E> joinBuilder;

        private WhereBuilder(Boolean isGroup, Class<E> clazz) {
            this.clazz = clazz;
            this.join = new StringBuilder();
            this.whereQuery = new StringBuilder(isGroup ? StringUtils.EMPTY : " WHERE ");
            this.joinBuilder = new JoinBuilder<>(clazz).builder();
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
            String leftJoin = createLeftJoin(joinTableName, clazz);
            if (!this.join.toString().contains(leftJoin)) {
                this.join.append(leftJoin);
            }
        }

        public WhereBuilder<E> group(WhereBuilder<E> where) {
            whereQuery.append("(")
                    .append(where.build().getWhereQuery())
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

        public Where build() {
            return new Where(join, whereQuery);
        }
    }
}