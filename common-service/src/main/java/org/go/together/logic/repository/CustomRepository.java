package org.go.together.logic.repository;

import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.logic.repository.utils.sql.CustomSqlBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public abstract class CustomRepository<E extends IdentifiedEntity> {
    @PersistenceContext
    private EntityManager entityManager;

    private CustomSqlBuilder<E> newQuery;

    @Transactional
    public E save(E entity) {
        return entityManager.merge(entity);
    }

    @Transactional
    public void delete(E entity) {
        entityManager.remove(entity);
    }

    @Transactional
    public Optional<E> findById(UUID uuid) {
        return Optional.ofNullable(entityManager.find(this.getEntityClass(), uuid));
    }

    @Transactional
    public Collection<E> findAll() {
        return createQuery().fetchAll();
    }

    public CustomSqlBuilder<E> createQuery() {
        CustomSqlBuilder<E> customSqlBuilder = new CustomSqlBuilder<>(getEntityClass(), entityManager);
        newQuery = customSqlBuilder;
        return customSqlBuilder;
    }

    public CustomSqlBuilder<E>.WhereBuilder createWhere() {
        return newQuery.new WhereBuilder(false);
    }

    public CustomSqlBuilder<E>.WhereBuilder createGroup() {
        return newQuery.new WhereBuilder(true);
    }

    private Class<E> getEntityClass() {
        Class clazz = this.getClass();

        do {
            Type genericSuperclass = clazz.getGenericSuperclass();
            boolean isParametrizedType = genericSuperclass instanceof ParameterizedType;
            if (isParametrizedType) {
                return (Class) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
            }

            clazz = clazz.getSuperclass();
        } while (clazz != null);

        throw new IllegalArgumentException("Class " + this.getClass() + " doesn't have a generic type.");
    }
}
