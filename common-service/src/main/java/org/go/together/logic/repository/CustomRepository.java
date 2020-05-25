package org.go.together.logic.repository;

import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.logic.repository.builder.SqlBuilder;

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

    private SqlBuilder<E> newQuery;

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

    public SqlBuilder<E> createQuery() {
        SqlBuilder<E> sqlBuilder = new SqlBuilder<>(getEntityClass(), entityManager, null);
        newQuery = sqlBuilder;
        return sqlBuilder;
    }

    public SqlBuilder<E> createQuery(String selectRow) {
        SqlBuilder<E> sqlBuilder = new SqlBuilder<>(getEntityClass(), entityManager, selectRow);
        newQuery = sqlBuilder;
        return sqlBuilder;
    }

    public SqlBuilder<E>.WhereBuilder createWhere() {
        return newQuery.new WhereBuilder(false);
    }

    public SqlBuilder<E>.WhereBuilder createGroup() {
        return newQuery.new WhereBuilder(true);
    }

    public Class<E> getEntityClass() {
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
