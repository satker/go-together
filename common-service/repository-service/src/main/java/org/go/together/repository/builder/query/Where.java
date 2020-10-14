package org.go.together.repository.builder.query;

import org.apache.commons.lang3.StringUtils;
import org.go.together.repository.builder.interfaces.Query;
import org.go.together.repository.builder.interfaces.WhereBuilder;
import org.go.together.repository.entities.IdentifiedEntity;
import org.go.together.repository.sql.SqlOperator;

import java.util.Map;

import static org.go.together.repository.builder.utils.BuilderUtils.createLeftJoin;
import static org.go.together.repository.sql.ObjectStringParser.parseToString;

public class Where implements Query {
    private final StringBuilder join;
    private final StringBuilder whereQuery;

    private Where(StringBuilder join, StringBuilder whereQuery) {
        this.join = join;
        this.whereQuery = whereQuery;
    }

    public static <E extends IdentifiedEntity> WhereBuilderImpl<E> builder() {
        return new WhereBuilderImpl<>();
    }

    public StringBuilder getWhereQuery() {
        return whereQuery;
    }

    public StringBuilder getJoin() {
        return join;
    }

    public static class WhereBuilderImpl<E extends IdentifiedEntity>
            implements WhereBuilder<E> {
        private static final String AND = " and ";
        private static final String OR = " or ";

        private Class<E> clazz;
        private final StringBuilder join;
        private StringBuilder whereQuery;
        private Join<E> joinBuilder;

        private WhereBuilderImpl() {
            this.join = new StringBuilder();
            this.whereQuery = new StringBuilder(" WHERE ");
        }

        public WhereBuilderImpl<E> clazz(Class<E> clazz) {
            this.clazz = clazz;
            this.joinBuilder = Join.<E>builder().clazz(clazz).build();
            return this;
        }

        public WhereBuilderImpl<E> isGroup(boolean isGroup) {
            if (isGroup) {
                this.whereQuery = new StringBuilder(StringUtils.EMPTY);
            }
            return this;
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

        public WhereBuilderImpl<E> group(WhereBuilder<E> where) {
            whereQuery.append("(")
                    .append(where.build().getWhereQuery())
                    .append(")");
            return this;
        }

        public WhereBuilderImpl<E> and() {
            whereQuery.append(AND);
            return this;
        }

        public WhereBuilderImpl<E> or() {
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
