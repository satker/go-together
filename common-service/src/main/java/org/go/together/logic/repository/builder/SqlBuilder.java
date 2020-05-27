package org.go.together.logic.repository.builder;

import org.apache.commons.lang3.StringUtils;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.Optional;

import static org.go.together.logic.repository.builder.utils.BuilderUtils.getEntityLink;

public class SqlBuilder<E extends IdentifiedEntity> {
    private final String from;
    private final StringBuilder query;
    private final EntityManager entityManager;
    private final Class<E> clazz;
    private final String select;

    public SqlBuilder(Class<E> clazz, EntityManager entityManager, String selectRow) {
        String entityLink = getEntityLink(clazz);
        String selectQuery = selectRow == null ? entityLink : entityLink + "." + selectRow;
        select = "select distinct " + selectQuery;
        from = " FROM " + clazz.getSimpleName() + " " + entityLink;
        query = new StringBuilder();
        this.entityManager = entityManager;
        this.clazz = clazz;
    }

    public SqlBuilder<E> where(WhereBuilder<E> whereBuilder) {
        String joinQuery = whereBuilder.getJoinQuery().toString();
        if (StringUtils.isNotEmpty(joinQuery)) {
            query.append(select);
            query.append(from);
            query.append(joinQuery);
        } else {
            query.append(from);
        }
        query.append(whereBuilder.getWhereQuery());
        return this;
    }

    public Collection<E> fetchAll() {
        return entityManager.createQuery(getQuery(), clazz).getResultList();
    }

    public Optional<E> fetchOne() {
        return entityManager.createQuery(getQuery(), clazz).getResultStream().findFirst();
    }

    public Collection<E> fetchWithPageable(int start, int pageSize) {
        TypedQuery<E> query = entityManager.createQuery(getQuery(), clazz);
        query.setFirstResult(start);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Collection<Object> fetchWithPageableNotDefined(int start, int pageSize) {
        Query query = entityManager.createQuery(getQuery(), Object.class);
        query.setFirstResult(start);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Collection<Object> fetchAllNotDefined() {
        return entityManager.createQuery(getQuery(), Object.class).getResultList();
    }

    public String getQuery() {
        if (StringUtils.isNotEmpty(query)) {
            return query.toString();
        } else {
            return from;
        }
    }

    public Number getCountRows() {
        return entityManager.createQuery("SELECT COUNT (DISTINCT " + getEntityLink(clazz) + ".id) FROM " +
                clazz.getSimpleName() + " " + getEntityLink(clazz), Number.class)
                .getSingleResult();
    }

    public Number getCountRowsWhere(WhereBuilder<E> whereBuilder) {
        String query = "SELECT COUNT (DISTINCT " + getEntityLink(clazz) + ".id) FROM " +
                clazz.getSimpleName() + " " + getEntityLink(clazz) + " " + whereBuilder.getJoinQuery() +
                whereBuilder.getWhereQuery();
        return entityManager.createQuery(query, Number.class).getSingleResult();
    }
}
