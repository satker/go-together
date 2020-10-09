package org.go.together.repository.builder;

import org.apache.commons.lang3.StringUtils;
import org.go.together.repository.entities.Direction;
import org.go.together.repository.entities.IdentifiedEntity;

import java.util.Map;
import java.util.stream.Collectors;

public class SortBuilder<E extends IdentifiedEntity> {
    private final Class<E> clazz;
    private JoinBuilder<E> joinBuilder;
    private StringBuilder join;

    public SortBuilder(Class<E> clazz) {
        this.clazz = clazz;
    }

    public SortBuilder<E> builder(StringBuilder join) {
        this.joinBuilder = new JoinBuilder<>(clazz).builder();
        this.join = join;
        return this;
    }

    public String getSortQuery(Map<String, Direction> sortMap) {
        return sortMap.entrySet().stream()
                .map(entry -> getJoinQuery(entry.getKey()) + StringUtils.SPACE + entry.getValue().toString())
                .collect(Collectors.joining(", ", " ORDER BY ", StringUtils.EMPTY));
    }

    private String getJoinQuery(String field) {
        return joinBuilder.getFieldWithJoin(field, this::enrichJoin);
    }

    private void enrichJoin(Map.Entry<String, String> joinTableName) {
        String leftJoin = joinBuilder.createLeftJoin(joinTableName);
        if (!this.join.toString().contains(leftJoin)) {
            this.join.append(leftJoin);
        }
    }
}
