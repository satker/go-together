package org.go.together.repository.builder;

import org.go.together.enums.SqlOperator;
import org.go.together.enums.SqlPredicate;
import org.go.together.model.IdentifiedEntity;
import org.go.together.repository.builders.Where;
import org.go.together.repository.query.WhereBuilder;
import org.go.together.repository.sql.OperatorParser;

import java.util.HashMap;
import java.util.Map;

import static org.go.together.repository.sql.ObjectStringParser.parseToString;

public class WhereImpl<E extends IdentifiedEntity> implements Where<E> {
    private final Map<String, String> joinMap;
    private final StringBuilder whereQuery;

    private WhereImpl(Map<String, String> joinMap, StringBuilder whereQuery) {
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
        private final Map<String, String> joinMap = new HashMap<>();
        private final StringBuilder whereQuery = new StringBuilder();
        private Join<B> joinBuilder;

        public WhereBuilderImpl<B> clazz(Class<B> clazz) {
            this.joinBuilder = Join.<B>builder().clazz(clazz).build();
            return this;
        }

        public void addJoin(Map<String, String> joinMap) {
            this.joinMap.putAll(joinMap);
        }

        public WhereBuilder<B> condition(String field, SqlOperator sqlOperator, Object value) {
            String parsedValue = parseToString(value);
            String fieldName = getFieldWithJoin(field);
            String query = OperatorParser.getOperatorFunction(sqlOperator, fieldName, parsedValue);
            whereQuery.append(query);
            return this;
        }

        private String getFieldWithJoin(String field) {
            return joinBuilder.getFieldWithJoin(field);
        }

        public WhereBuilderImpl<B> group(WhereBuilder<B> where) {
            Where<B> build = where.build();
            joinMap.putAll(build.getJoin());
            whereQuery.append("(")
                    .append(build.getWhereQuery())
                    .append(")");
            return this;
        }

        public WhereBuilderImpl<B> and() {
            whereQuery.append(SqlPredicate.AND.getDescription());
            return this;
        }

        public WhereBuilderImpl<B> or() {
            whereQuery.append(SqlPredicate.OR.getDescription());
            return this;
        }

        public void cutLastAnd() {
            if (whereQuery.toString().endsWith(SqlPredicate.AND.getDescription())) {
                whereQuery.setLength(whereQuery.length() - SqlPredicate.AND.getDescription().length());
            }
        }

        public void cutLastOr() {
            if (whereQuery.toString().endsWith(SqlPredicate.OR.getDescription())) {
                whereQuery.setLength(whereQuery.length() - SqlPredicate.OR.getDescription().length());
            }
        }

        public WhereImpl<B> build() {
            joinMap.putAll(joinBuilder.getJoinTables());
            return new WhereImpl<>(joinMap, whereQuery);
        }
    }
}
