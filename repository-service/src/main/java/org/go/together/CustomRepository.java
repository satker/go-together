package org.go.together;

import org.go.together.builder.SqlBuilder;
import org.go.together.builder.WhereBuilder;
import org.go.together.interfaces.IdentifiedEntity;

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

    private final Class<E> clazz = getEntityClass();

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
        return new SqlBuilder<>(getEntityClass(), entityManager, null, null);
    }

    public SqlBuilder<E> createQuery(String selectRow, Integer havingCondition) {
        return new SqlBuilder<>(getEntityClass(), entityManager, selectRow, havingCondition);
    }

    public WhereBuilder<E> createWhere() {
        return new WhereBuilder<>(false, clazz);
    }

    public WhereBuilder<E> createGroup() {
        return new WhereBuilder<>(true, clazz);
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