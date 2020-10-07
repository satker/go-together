package org.go.together.repository.builder;

import org.apache.commons.lang3.StringUtils;
import org.go.together.repository.entities.Direction;
import org.go.together.repository.entities.IdentifiedEntity;

import java.util.Map;
import java.util.stream.Collectors;

public class SortBuilder<E extends IdentifiedEntity> {
    private final JoinBuilder<E> joinBuilder;
    private final StringBuilder join;

    public SortBuilder(StringBuilder join, Class<E> clazz) {
        this.joinBuilder = new JoinBuilder<>(clazz);
        this.join = join;
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
