package org.go.together.repository.builder;

import org.apache.commons.lang3.StringUtils;
import org.go.together.repository.entities.Direction;
import org.go.together.repository.entities.IdentifiedEntity;
import org.go.together.repository.interfaces.Query;
import org.go.together.repository.interfaces.SqlBuilder;
import org.go.together.repository.interfaces.WhereBuilder;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.go.together.repository.builder.utils.BuilderUtils.getEntityLink;

public class Sql<E extends IdentifiedEntity> implements Query<E> {
    private final EntityManager entityManager;
    private final Class<E> clazz;
    private final String query;
    private final String countQuery;

    private Sql(Class<E> clazz, EntityManager entityManager, String query, String countQuery) {
        this.entityManager = entityManager;
        this.clazz = clazz;
        this.query = query;
        this.countQuery = countQuery;
    }

    public static <E extends IdentifiedEntity> SqlBuilderImpl<E> builder() {
        return new SqlBuilderImpl<>();
    }

    public String getQuery() {
        return this.query;
    }

    public Collection<E> fetchAll() {
        TypedQuery<E> query = entityManager.createQuery(this.query, clazz);
        return getResult(query);
    }

    public Optional<E> fetchOne() {
        return entityManager.createQuery(this.query, clazz).getResultStream().findFirst();
    }

    public Collection<E> fetchWithPageable(int start, int pageSize) {
        TypedQuery<E> query = entityManager.createQuery(this.query, clazz);
        query.setFirstResult(start);
        query.setMaxResults(pageSize);
        return getResult(query);
    }

    public Collection<Object> fetchWithPageableNotDefined(int start, int pageSize) {
        TypedQuery<Object> query = entityManager.createQuery(this.query, Object.class);
        query.setFirstResult(start);
        query.setMaxResults(pageSize);
        return getResult(query);
    }

    public Collection<Object> fetchAllNotDefined() {
        TypedQuery<Object> query = entityManager.createQuery(this.query, Object.class);
        return getResult(query);
    }

    public Long getCountRows() {
        try {
            return entityManager.createQuery(countQuery, Long.class).getSingleResult();
        } catch (NoResultException e) {
            return 0L;
        }
    }

    private <T> Collection<T> getResult(TypedQuery<T> typedQuery) {
        return typedQuery.getResultList();
    }

    public static class SqlBuilderImpl<B extends IdentifiedEntity> implements SqlBuilder<B> {
        private String from;
        private Class<B> clazz;
        private EntityManager entityManager;
        private String selectRow;
        private String havingCondition;
        private StringBuilder join;
        private final StringBuilder query;
        private final StringBuilder sort;
        private Map<String, Direction> sortMap = new HashMap<>();

        private SqlBuilderImpl() {
            this.join = new StringBuilder();
            this.query = new StringBuilder();
            this.sort = new StringBuilder();
        }

        public SqlBuilderImpl<B> clazz(Class<B> clazz) {
            this.clazz = clazz;
            this.selectRow = getEntityLink(clazz);
            this.from = " FROM " + clazz.getSimpleName() + StringUtils.SPACE + getEntityLink(clazz);
            return this;
        }

        public SqlBuilderImpl<B> entityManager(EntityManager entityManager) {
            this.entityManager = entityManager;
            return this;
        }

        public SqlBuilderImpl<B> having(Integer havingCondition) {
            if (havingCondition != null && selectRow != null) {
                this.havingCondition = " group by " + selectRow +
                        " having count(" + selectRow + ") = " + havingCondition;
            } else {
                this.havingCondition = StringUtils.EMPTY;
            }
            return this;
        }

        public SqlBuilderImpl<B> select(String selectRow) {
            String entityLink = getEntityLink(clazz);
            this.selectRow = Optional.ofNullable(selectRow)
                    .map((row) -> entityLink + "." + selectRow)
                    .orElse(entityLink);
            return this;
        }

        public SqlBuilderImpl<B> where(WhereBuilder<B> where) {
            Where<B> buildWhere = where.build();
            join.append(buildWhere.getJoin());
            query.append(buildWhere.getWhereQuery());
            return this;
        }

        public SqlBuilderImpl<B> sort(String key, Direction direction) {
            this.sortMap.put(key, direction);
            return this;
        }

        private void enrichBySort() {
            Sort<B> sortDto = Sort.<B>builder().clazz(clazz).join(join).sort(sortMap).build();
            sort.append(sortDto.getSortQuery());
            join = new StringBuilder(sortDto.getJoin());
        }

        private String getCountQuery() {
            StringBuilder query = new StringBuilder().append("SELECT COUNT (DISTINCT ")
                    .append(selectRow)
                    .append(") FROM ")
                    .append(clazz.getSimpleName())
                    .append(StringUtils.SPACE)
                    .append(getEntityLink(clazz))
                    .append(StringUtils.SPACE)
                    .append((StringUtils.isNotBlank(join) ? join : StringUtils.EMPTY))
                    .append(StringUtils.isNotBlank(this.query) ? this.query : StringUtils.EMPTY);
            if (havingCondition != null && StringUtils.isNotBlank(selectRow)) {
                query.append(havingCondition);
            }
            return query.toString();
        }

        public Sql<B> build() {
            if (!sortMap.isEmpty()) {
                enrichBySort();
            }
            return new Sql<>(clazz, entityManager, getQuery(), getCountQuery());
        }

        private String getQuery() {
            StringBuilder result = new StringBuilder();
            result.append(getFirstQueryPart());
            if (StringUtils.isNotBlank(query)) {
                result.append(query);
            }
            if (StringUtils.isNotBlank(havingCondition)) {
                result.append(havingCondition);
            }
            if (StringUtils.isNotBlank(sort)) {
                result.append(sort);
            }
            return result.toString();
        }

        private String getFirstQueryPart() {
            final String SELECT = "select ";
            if (StringUtils.isNotEmpty(join)) {
                return SELECT + "distinct " + selectRow + from + join;
            } else {
                String result = StringUtils.EMPTY;
                if (!selectRow.equals(getEntityLink(clazz))) {
                    result = SELECT + selectRow;
                }
                return result.concat(from);
            }
        }
    }
}
