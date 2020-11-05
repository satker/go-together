package org.go.together.repository;

import org.go.together.base.CustomRepository;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.model.IdentifiedEntity;
import org.go.together.repository.builder.SqlImpl;
import org.go.together.repository.builder.WhereImpl;
import org.go.together.repository.query.SqlBuilder;
import org.go.together.repository.query.WhereBuilder;
import org.go.together.utils.ReflectionUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Transactional
public abstract class CustomRepositoryImpl<E extends IdentifiedEntity> implements CustomRepository<E> {
    private final Class<E> clazz = ReflectionUtils.getParametrizedClass(this.getClass(), 0);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
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
    public E save(E entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void delete(E entity) {
        entityManager.remove(entity);
    }

    @Override
    public Optional<E> findById(UUID uuid) {
        return Optional.ofNullable(entityManager.find(clazz, uuid));
    }

    @Override
    public E findByIdOrThrow(UUID uuid) {
        return Optional.ofNullable(entityManager.find(clazz, uuid))
                .orElseThrow(() -> new CannotFindEntityException("Cannot find " + clazz.getSimpleName() + " by id " + uuid));
    }

    @Override
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
        return SqlImpl.<E>builder().clazz(clazz).entityManager(entityManager).having(havingCondition).select(selectRow);
    }

    @Override
    public WhereBuilder<E> createWhere() {
        return WhereImpl.<E>builder().clazz(clazz);
    }
}
