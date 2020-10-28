package org.go.together.repository.interfaces;

import org.go.together.repository.entities.IdentifiedEntity;

public interface QueryClassBuilder<E extends IdentifiedEntity> {
    QueryBuilder<E> clazz(Class<E> clazz);
}