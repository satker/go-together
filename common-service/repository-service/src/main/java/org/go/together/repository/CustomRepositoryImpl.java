package org.go.together.repository;

import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.repository.builder.Sql;
import org.go.together.repository.builder.Where;
import org.go.together.repository.entities.IdentifiedEntity;
import org.go.together.repository.interfaces.SqlBuilder;
import org.go.together.repository.interfaces.WhereBuilder;
import org.go.together.utils.ReflectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public abstract class CustomRepositoryImpl<E extends IdentifiedEntity> implements CustomRepository<E> {
    private final Class<E> clazz = ReflectionUtils.getParametrizedClass(this.getClass(), 0);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public E create() {
        E entity;
        try {
            entity = clazz.getConstructor().newInstance();
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException("Cannot create instance entity");
        }
        entity.setId(UUID.randomUUID());
        return entityManager.merge(entity);
    }

    @Override
    @Transactional
    public E save(E entity) {
        return entityManager.merge(entity);
    }

    @Override
    @Transactional
    public void delete(E entity) {
        entityManager.remove(entity);
    }

    @Override
    @Transactional
    public Optional<E> findById(UUID uuid) {
        return Optional.ofNullable(entityManager.find(clazz, uuid));
    }

    @Override
    @Transactional
    public E findByIdOrThrow(UUID uuid) {
        return Optional.ofNullable(entityManager.find(clazz, uuid))
                .orElseThrow(() -> new CannotFindEntityException("Cannot find " + clazz.getSimpleName() + " by id " + uuid));
    }

    @Override
    @Transactional
    public Collection<E> findAll() {
        return createQuery().build().fetchAll();
    }

    @Override
    public SqlBuilder<E> createQuery() {
        return createQuery(null, null);
    }

    @Override
    public SqlBuilder<E> createQuery(String selectRow) {
        return createQuery(selectRow, null);
    }

    @Override
    public SqlBuilder<E> createQuery(String selectRow, Integer havingCondition) {
        return Sql.<E>builder().clazz(clazz).entityManager(entityManager).having(havingCondition).select(selectRow);
    }

    @Override
    public WhereBuilder<E> createWhere() {
        return Where.<E>builder().clazz(clazz);
    }
}
