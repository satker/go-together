package org.go.together.repository.builder;

import org.go.together.enums.Direction;
import org.go.together.model.IdentifiedEntity;
import org.go.together.repository.builders.Query;
import org.go.together.repository.interfaces.SortBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Sort<E extends IdentifiedEntity> implements Query<E> {
    private final Map<String, String> joinMap;
    private final Map<String, Direction> sortMap;

    public Sort(Map<String, String> joinMap, Map<String, Direction> sortMap) {
        this.joinMap = joinMap;
        this.sortMap = sortMap;
    }

    public static <E extends IdentifiedEntity> SortBuilderImpl<E> builder() {
        return new SortBuilderImpl<>();
    }

    public Map<String, Direction> getSortMap() {
        return sortMap;
    }

    public Map<String, String> getJoin() {
        return joinMap;
    }

    public static class SortBuilderImpl<B extends IdentifiedEntity> implements SortBuilder<B> {
        private Join<B> joinBuilder;
        private final Map<String, String> joinMap = new HashMap<>();
        private final Map<String, Direction> fixedSortMap = new HashMap<>();

        public SortBuilderImpl<B> clazz(Class<B> clazz) {
            this.joinBuilder = Join.<B>builder().clazz(clazz).build();
            return this;
        }

        public SortBuilderImpl<B> join(Map<String, String> joinMap) {
            this.joinMap.putAll(joinMap);
            return this;
        }

        public SortBuilderImpl<B> sort(Map<String, Direction> sortMap) {
            Map<String, Direction> currentFixedMap = sortMap.entrySet().stream()
                    .collect(Collectors.toMap(entry -> getJoinQuery(entry.getKey()), Map.Entry::getValue));
            fixedSortMap.putAll(currentFixedMap);
            return this;
        }

        public Sort<B> build() {
            joinMap.putAll(joinBuilder.getJoinTables());
            return new Sort<>(joinMap, fixedSortMap);
        }

        private String getJoinQuery(String field) {
            return joinBuilder.getFieldWithJoin(field);
        }
    }
}
