package org.go.together.repository.builders;

import org.go.together.model.IdentifiedEntity;

public interface QueryClassBuilder<E extends IdentifiedEntity> {
    QueryBuilder<E> clazz(Class<E> clazz);
}
