package org.go.together.repository.builder;

import org.apache.commons.lang3.StringUtils;
import org.go.together.repository.entities.IdentifiedEntity;
import org.go.together.repository.interfaces.Query;
import org.go.together.repository.interfaces.WhereBuilder;
import org.go.together.repository.sql.SqlOperator;

import java.util.HashMap;
import java.util.Map;

import static org.go.together.repository.sql.ObjectStringParser.parseToString;

public class Where<E extends IdentifiedEntity> implements Query<E> {
    private final Map<String, String> joinMap;
    private final StringBuilder whereQuery;

    private Where(Map<String, String> joinMap, StringBuilder whereQuery) {
        this.joinMap = joinMap;
        this.whereQuery = whereQuery;
    }

    public static <E extends IdentifiedEntity> WhereBuilderImpl<E> builder() {
        return new WhereBuilderImpl<>();
    }

    public StringBuilder getWhereQuery() {
        return whereQuery;
    }

    public Map<String, String> getJoin() {
        return joinMap;
    }

    public static class WhereBuilderImpl<B extends IdentifiedEntity>
            implements WhereBuilder<B> {
        private static final String AND = " and ";
        private static final String OR = " or ";

        private final Map<String, String> joinMap;
        private StringBuilder whereQuery;
        private Join<B> joinBuilder;

        private WhereBuilderImpl() {
            this.joinMap = new HashMap<>();
            this.whereQuery = new StringBuilder(" WHERE ");
        }

        public WhereBuilderImpl<B> clazz(Class<B> clazz) {
            this.joinBuilder = Join.<B>builder().clazz(clazz).build();
            return this;
        }

        public WhereBuilderImpl<B> isGroup(boolean isGroup) {
            if (isGroup) {
                this.whereQuery = new StringBuilder(StringUtils.EMPTY);
            }
            return this;
        }

        public void addJoin(Map<String, String> joinMap) {
            this.joinMap.putAll(joinMap);
        }

        public WhereBuilder<B> condition(String field, SqlOperator sqlOperator, Object value) {
            String parsedValue = parseToString(value);
            String fieldName = getFieldWithJoin(field);
            whereQuery.append(sqlOperator.getBiFunction().apply(fieldName, parsedValue));
            return this;
        }

        private String getFieldWithJoin(String field) {
            return joinBuilder.getFieldWithJoin(field);
        }

        public WhereBuilderImpl<B> group(WhereBuilder<B> where) {
            whereQuery.append("(")
                    .append(where.build().getWhereQuery())
                    .append(")");
            return this;
        }

        public WhereBuilderImpl<B> and() {
            whereQuery.append(AND);
            return this;
        }

        public WhereBuilderImpl<B> or() {
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

        public Where<B> build() {
            joinMap.putAll(joinBuilder.getJoinTables());
            return new Where<>(joinMap, whereQuery);
        }
    }
}
