package org.go.together.repository;

import org.go.together.repository.entities.IdentifiedEntity;
import org.go.together.repository.interfaces.SqlBuilder;
import org.go.together.repository.interfaces.WhereBuilder;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface CustomRepository<E extends IdentifiedEntity> {
    E create();

    E save(E entity);

    void delete(E entity);

    Optional<E> findById(UUID uuid);

    E findByIdOrThrow(UUID uuid);

    Collection<E> findAll();

    SqlBuilder<E> createQuery();

    SqlBuilder<E> createQuery(String selectRow);

    SqlBuilder<E> createQuery(String selectRow, Integer havingCondition);

    WhereBuilder<E> createWhere();
}
