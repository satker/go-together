package org.go.together.repository.builder;

import org.apache.commons.lang3.StringUtils;
import org.go.together.repository.builder.dto.SortDto;
import org.go.together.repository.builder.dto.SqlDto;
import org.go.together.repository.entities.Direction;
import org.go.together.repository.entities.IdentifiedEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static org.go.together.repository.builder.utils.BuilderUtils.getEntityLink;

public class SqlBuilder<E extends IdentifiedEntity> {
    private String from;
    private StringBuilder query;
    private final EntityManager entityManager;
    private final Class<E> clazz;
    private String selectRow;
    private StringBuilder havingCondition;
    private StringBuilder sort;
    private StringBuilder join;

    public SqlBuilder(Class<E> clazz, EntityManager entityManager) {
        this.entityManager = entityManager;
        this.clazz = clazz;
    }

    public SqlBuilder<E> builder(String selectRow, Integer havingCondition) {
        String entityLink = getEntityLink(clazz);
        String selectQuery = Optional.ofNullable(selectRow)
                .map((row) -> entityLink + "." + selectRow)
                .orElse(entityLink);
        this.selectRow = selectQuery;
        this.from = " FROM " + clazz.getSimpleName() + StringUtils.SPACE + entityLink;
        this.query = new StringBuilder();
        this.sort = new StringBuilder();
        this.join = new StringBuilder();
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
        return this;
    }

    public SqlDto build() {
        return SqlDto.builder().havingCondition(havingCondition.toString())
                .selectRow(selectRow)
                .query(getQuery())
                .build();
    }

    public SqlBuilder<E> where(Where.WhereBuilder<E> where) {
        Where buildWhere = where.build();
        join.append(buildWhere.getJoin());
        query.append(buildWhere.getWhereQuery());
        return this;
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

    public Number getCountRows() {
        String query = "SELECT COUNT (DISTINCT " +
                getEntityLink(clazz) +
                ".id) FROM " +
                clazz.getSimpleName() +
                StringUtils.SPACE +
                getEntityLink(clazz);
        return entityManager.createQuery(query, Number.class)
                .getResultStream().count();
    }

    public Number getCountRowsWhere(Where.WhereBuilder<E> where, String selectRow, String having) {
        Where buildWhere = where.build();
        StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT (DISTINCT ")
                .append(selectRow)
                .append(") FROM ")
                .append(clazz.getSimpleName())
                .append(StringUtils.SPACE)
                .append(getEntityLink(clazz))
                .append(StringUtils.SPACE)
                .append(buildWhere.getJoin())
                .append(buildWhere.getWhereQuery());
        if (having != null && StringUtils.isNotBlank(selectRow)) {
            query.append(having);
        }
        return entityManager.createQuery(query.toString(), Number.class).getResultStream().count();
    }

    public SqlBuilder<E> sort(Map<String, Direction> sortMap) {
        SortDto sortDto = new SortBuilder<>(clazz).builder(join).sortQuery(sortMap).build();
        sort.append(sortDto.getSortQuery());
        join.append(sortDto.getJoin());
        return this;
    }

    private <T> Collection<T> getResult(TypedQuery<T> typedQuery) {
        return typedQuery.getResultList();
    }

    private String getQuery() {
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

    private String getFirstQueryPart() {
        final String SELECT = "select " + selectRow;
        if (StringUtils.isNotEmpty(join)) {
            return SELECT + from + join;
        } else {
            String result = StringUtils.EMPTY;
            if (!selectRow.equals(getEntityLink(clazz))) {
                result = SELECT;
            }
            return result.concat(from);
        }
    }
}
