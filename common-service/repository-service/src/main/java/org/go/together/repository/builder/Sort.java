package org.go.together.repository.builder;

import org.apache.commons.lang3.StringUtils;
import org.go.together.repository.entities.Direction;
import org.go.together.repository.entities.IdentifiedEntity;
import org.go.together.repository.interfaces.Query;
import org.go.together.repository.interfaces.SortBuilder;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.go.together.repository.builder.utils.BuilderUtils.createLeftJoin;

public class Sort<E extends IdentifiedEntity> implements Query<E> {
    private final String join;
    private final String sortQuery;

    public Sort(String join, String sortQuery) {
        this.join = join;
        this.sortQuery = sortQuery;
    }

    public static <E extends IdentifiedEntity> SortBuilderImpl<E> builder() {
        return new SortBuilderImpl<>();
    }

    public String getSortQuery() {
        return sortQuery;
    }

    public String getJoin() {
        return join;
    }

    public static class SortBuilderImpl<B extends IdentifiedEntity> implements SortBuilder<B> {
        private Class<B> clazz;
        private Join<B> joinBuilder;
        private StringBuilder join;
        private String sortQuery;

        public SortBuilderImpl<B> clazz(Class<B> clazz) {
            this.clazz = clazz;
            this.joinBuilder = Join.<B>builder().clazz(clazz).build();
            return this;
        }

        public SortBuilderImpl<B> join(StringBuilder join) {
            this.join = join;
            return this;
        }

        public SortBuilderImpl<B> sort(Map<String, Direction> sortMap) {
            this.sortQuery = sortMap.entrySet().stream()
                    .map(entry -> getJoinQuery(entry.getKey()) + StringUtils.SPACE + entry.getValue().toString())
                    .collect(Collectors.joining(", ", " ORDER BY ", StringUtils.EMPTY));
            return this;
        }

        public Sort<B> build() {
            return new Sort<>(Optional.ofNullable(join).map(StringBuilder::toString).orElse(StringUtils.EMPTY), sortQuery);
        }

        private String getJoinQuery(String field) {
            return joinBuilder.getFieldWithJoin(field, this::enrichJoin);
        }

        private void enrichJoin(Map.Entry<String, String> joinTableName) {
            String leftJoin = createLeftJoin(joinTableName, clazz);
            if (!this.join.toString().contains(leftJoin)) {
                this.join.append(leftJoin);
            }
        }
    }
}
