package org.go.together.logic.repository.utils.sql;

import org.go.together.interfaces.IdentifiedEntity;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

import static org.go.together.logic.repository.utils.sql.ObjectStringParser.parseToString;

public class CustomBuilder<E extends IdentifiedEntity> {
    private final StringBuilder query;
    private final Session session;

    public CustomBuilder(Class<E> clazz, Session session) {
        query = new StringBuilder("FROM " + clazz.getSimpleName());
        this.session = session;
    }

    public CustomBuilder where(WhereBuilder whereBuilder) {
        query.append(whereBuilder.getWhereQuery());
        return this;
    }

    public List<E> fetchAll() {
        return session.createQuery(query.toString()).getResultList();
    }

    public Optional<E> fetchOne() {
        return session.createQuery(query.toString()).setMaxResults(1).stream().findFirst();
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
