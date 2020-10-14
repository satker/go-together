package org.go.together.repository.interfaces;

import org.go.together.repository.builder.Sort;
import org.go.together.repository.entities.Direction;
import org.go.together.repository.entities.IdentifiedEntity;

import java.util.Map;

public interface SortBuilder<E extends IdentifiedEntity> extends QueryBuilder<E> {
    SortBuilder<E> sort(Map<String, Direction> sortMap);

    Sort<E> build();
}
