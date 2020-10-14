package org.go.together.repository;

import org.go.together.repository.builder.Sql;
import org.go.together.repository.builder.Where;
import org.go.together.repository.entities.IdentifiedEntity;

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

    Sql.SqlBuilder<E> createQuery();

    Sql.SqlBuilder<E> createQuery(String selectRow);

    Sql.SqlBuilder<E> createQuery(String selectRow, Integer havingCondition);

    Where.WhereBuilder<E> createWhere();

    Where.WhereBuilder<E> createGroup();
}
