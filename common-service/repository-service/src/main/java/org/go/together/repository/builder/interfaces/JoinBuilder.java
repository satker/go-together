package org.go.together.repository.builder.interfaces;

import org.go.together.repository.builder.query.Join;
import org.go.together.repository.entities.IdentifiedEntity;

public interface JoinBuilder<E extends IdentifiedEntity> extends QueryBuilder<E> {
    Join<E> build();
}
