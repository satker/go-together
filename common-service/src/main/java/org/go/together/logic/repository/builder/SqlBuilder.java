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
    private final String selectRow;
    private final StringBuilder havingCondition;

    public SqlBuilder(Class<E> clazz, EntityManager entityManager, String selectRow, Integer havingCondition) {
        String entityLink = getEntityLink(clazz);
        String selectQuery = selectRow == null ? entityLink : entityLink + "." + selectRow;
        this.selectRow = selectQuery;
        select = "select distinct " + selectQuery;
        from = " FROM " + clazz.getSimpleName() + " " + entityLink;
        query = new StringBuilder();
        this.entityManager = entityManager;
        this.clazz = clazz;
        if (havingCondition != null && selectRow != null) {
            this.havingCondition = new StringBuilder().append(" group by ")
                    .append(selectQuery)
                    .append(" having count(")
                    .append(selectQuery)
                    .append(") = ")
                    .append(havingCondition);
        } else {
            this.havingCondition = new StringBuilder();
        }
    }

    public String getHaving() {
        return havingCondition.toString();
    }

    public String getSelectRow() {
        return selectRow;
    }

    public SqlBuilder<E> where(WhereBuilder<E> whereBuilder) {
        String joinQuery = whereBuilder.getJoinQuery().toString();
        if (StringUtils.isNotEmpty(joinQuery)) {
            query.append(select);
            query.append(from);
            query.append(joinQuery);
        } else {
            if (StringUtils.isNotBlank(selectRow)) {
                query.append("select ")
                        .append(selectRow);
            }
            query.append(from);
        }
        query.append(whereBuilder.getWhereQuery());
        if (havingCondition != null) {
            query.append(havingCondition);
        }
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
        Query query = entityManager.createQuery(getQuery());
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
        StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT (DISTINCT ")
                .append(getEntityLink(clazz))
                .append(".id) FROM ")
                .append(clazz.getSimpleName())
                .append(" ")
                .append(getEntityLink(clazz));
        return entityManager.createQuery(query.toString(), Number.class)
                .getResultStream().count();
    }

    public Number getCountRowsWhere(WhereBuilder<E> whereBuilder, String selectRow, String having) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT (DISTINCT ")
                .append(selectRow)
                .append(") FROM ")
                .append(clazz.getSimpleName())
                .append(" ")
                .append(getEntityLink(clazz))
                .append(" ")
                .append(whereBuilder.getJoinQuery())
                .append(whereBuilder.getWhereQuery());
        if (having != null && StringUtils.isNotBlank(selectRow)) {
            query.append(having);
        }
        return entityManager.createQuery(query.toString(), Number.class).getResultStream().count();
    }
}
