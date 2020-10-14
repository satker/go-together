package org.go.together.repository.interfaces;

import org.go.together.repository.builder.Join;
import org.go.together.repository.entities.IdentifiedEntity;

public interface JoinBuilder<E extends IdentifiedEntity> extends QueryBuilder<E> {
    Join<E> build();
}
