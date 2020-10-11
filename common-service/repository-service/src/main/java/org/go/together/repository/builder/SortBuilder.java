package org.go.together.repository.builder;

import org.apache.commons.lang3.StringUtils;
import org.go.together.repository.builder.dto.SortDto;
import org.go.together.repository.entities.Direction;
import org.go.together.repository.entities.IdentifiedEntity;

import java.util.Map;
import java.util.stream.Collectors;

import static org.go.together.repository.builder.utils.BuilderUtils.createLeftJoin;

public class SortBuilder<E extends IdentifiedEntity> {
    private final Class<E> clazz;
    private JoinBuilder<E> joinBuilder;
    private StringBuilder join;
    private String sortQuery;

    public SortBuilder(Class<E> clazz) {
        this.clazz = clazz;
    }

    public SortBuilder<E> builder(StringBuilder join) {
        this.joinBuilder = new JoinBuilder<>(clazz).builder();
        this.join = join;
        return this;
    }

    public SortBuilder<E> sortQuery(Map<String, Direction> sortMap) {
        this.sortQuery = sortMap.entrySet().stream()
                .map(entry -> getJoinQuery(entry.getKey()) + StringUtils.SPACE + entry.getValue().toString())
                .collect(Collectors.joining(", ", " ORDER BY ", StringUtils.EMPTY));
        return this;
    }

    public SortDto build() {
        return SortDto.builder().join(join).sortQuery(sortQuery).build();
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
