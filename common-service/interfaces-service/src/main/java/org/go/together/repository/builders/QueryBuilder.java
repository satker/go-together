package org.go.together.repository.builders;

import org.go.together.model.IdentifiedEntity;

public interface QueryBuilder<E extends IdentifiedEntity> extends QueryClassBuilder<E> {
    Query<E> build();
}
