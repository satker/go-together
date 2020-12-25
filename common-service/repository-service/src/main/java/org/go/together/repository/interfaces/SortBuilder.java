package org.go.together.repository.interfaces;

import org.go.together.enums.Direction;
import org.go.together.model.IdentifiedEntity;
import org.go.together.repository.builder.Sort;
import org.go.together.repository.builders.QueryBuilder;

import java.util.Map;

public interface SortBuilder<E extends IdentifiedEntity> extends QueryBuilder<E> {
    SortBuilder<E> sort(Map<String, Direction> sortMap);

    Sort<E> build();
}
