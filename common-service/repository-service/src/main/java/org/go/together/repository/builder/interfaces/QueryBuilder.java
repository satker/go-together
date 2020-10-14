package org.go.together.repository.builder.interfaces;

import org.go.together.repository.entities.IdentifiedEntity;

public interface QueryBuilder<E extends IdentifiedEntity> extends QueryClassBuilder<E> {
    Query<E> build();
}
