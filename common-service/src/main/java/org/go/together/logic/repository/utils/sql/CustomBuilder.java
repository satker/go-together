package org.go.together.logic.repository.utils.sql;

import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.go.together.logic.repository.utils.sql.ObjectStringParser.parseToString;

public class CustomBuilder<E extends IdentifiedEntity> {
    private final StringBuilder query;
    private final EntityManager entityManager;
    private final Class<E> clazz;

    public CustomBuilder(Class<E> clazz, EntityManager entityManager) {
        query = new StringBuilder("FROM " + clazz.getSimpleName());
        this.entityManager = entityManager;
        this.clazz = clazz;
    }

    public CustomBuilder<E> where(WhereBuilder whereBuilder) {
        query.append(whereBuilder.getWhereQuery());
        return this;
    }

    @Transactional
    public List<E> fetchAll() {
        return entityManager.createQuery(query.toString(), clazz).getResultList();
    }

    @Transactional
    public Optional<E> fetchOne() {
        return entityManager.createQuery(query.toString(), clazz).getResultStream().findFirst();
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
