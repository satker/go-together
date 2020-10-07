package org.go.together.repository.builder;

import org.apache.commons.lang3.StringUtils;
import org.go.together.repository.entities.Direction;
import org.go.together.repository.entities.IdentifiedEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static org.go.together.repository.builder.utils.BuilderUtils.getEntityLink;

public class SqlBuilder<E extends IdentifiedEntity> {
    private final String from;
    private final StringBuilder query;
    private final EntityManager entityManager;
    private final Class<E> clazz;
    private final String selectRow;
    private final StringBuilder havingCondition;
    private final StringBuilder sort;
    private final StringBuilder join;

    public SqlBuilder(Class<E> clazz, EntityManager entityManager, String selectRow, Integer havingCondition) {
        String entityLink = getEntityLink(clazz);
        String selectQuery = Optional.ofNullable(selectRow)
                .map((row) -> entityLink + "." + selectRow)
                .orElse(entityLink);
        this.selectRow = selectQuery;
        this.from = " FROM " + clazz.getSimpleName() + StringUtils.SPACE + entityLink;
        this.query = new StringBuilder();
        this.sort = new StringBuilder();
        this.join = new StringBuilder();
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
        join.append(whereBuilder.getJoinQuery());
        query.append(whereBuilder.getWhereQuery());
        return this;
    }

    private String getFirstQueryPart() {
        final String SELECT = "select " + selectRow;
        if (StringUtils.isNotEmpty(join)) {
            return SELECT + from + join;
        } else {
            String result = StringUtils.EMPTY;
            if (!selectRow.equals(getEntityLink(clazz))) {
                result = result.concat(SELECT).concat(selectRow);
            }
            return result.concat(from);
        }
    }

    public Collection<E> fetchAll() {
        TypedQuery<E> query = entityManager.createQuery(getQuery(), clazz);
        return getResult(query);
    }

    public Optional<E> fetchOne() {
        return entityManager.createQuery(getQuery(), clazz).getResultStream().findFirst();
    }

    public Collection<E> fetchWithPageable(int start, int pageSize) {
        TypedQuery<E> query = entityManager.createQuery(getQuery(), clazz);
        query.setFirstResult(start);
        query.setMaxResults(pageSize);
        return getResult(query);
    }

    public Collection<Object> fetchWithPageableNotDefined(int start, int pageSize) {
        TypedQuery<Object> query = entityManager.createQuery(getQuery(), Object.class);
        query.setFirstResult(start);
        query.setMaxResults(pageSize);
        return getResult(query);
    }

    public Collection<Object> fetchAllNotDefined() {
        TypedQuery<Object> query = entityManager.createQuery(getQuery(), Object.class);
        return getResult(query);
    }

    private <T> Collection<T> getResult(TypedQuery<T> typedQuery) {
        return typedQuery.getResultList();
    }

    public String getQuery() {
        StringBuilder result = new StringBuilder();
        result.append(getFirstQueryPart());
        if (StringUtils.isNotEmpty(query)) {
            result.append(query.toString());
        }
        if (havingCondition != null) {
            result.append(havingCondition);
        }
        if (StringUtils.isNotBlank(sort)) {
            result.append(sort);
        }
        return result.toString();
    }

    public Number getCountRows() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT (DISTINCT ")
                .append(getEntityLink(clazz))
                .append(".id) FROM ")
                .append(clazz.getSimpleName())
                .append(StringUtils.SPACE)
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
                .append(StringUtils.SPACE)
                .append(getEntityLink(clazz))
                .append(StringUtils.SPACE)
                .append(whereBuilder.getJoinQuery())
                .append(whereBuilder.getWhereQuery());
        if (having != null && StringUtils.isNotBlank(selectRow)) {
            query.append(having);
        }
        return entityManager.createQuery(query.toString(), Number.class).getResultStream().count();
    }

    public SqlBuilder<E> sort(Map<String, Direction> sortMap) {
        SortBuilder<E> sortBuilder = new SortBuilder<>(join, clazz);
        sort.append(sortBuilder.getSortQuery(sortMap));
        return this;
    }
}
