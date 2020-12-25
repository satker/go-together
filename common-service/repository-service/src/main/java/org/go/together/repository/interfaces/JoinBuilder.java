package org.go.together.repository.interfaces;

import org.go.together.model.IdentifiedEntity;
import org.go.together.repository.builder.Join;
import org.go.together.repository.builders.QueryBuilder;

public interface JoinBuilder<E extends IdentifiedEntity> extends QueryBuilder<E> {
    Join<E> build();
}
