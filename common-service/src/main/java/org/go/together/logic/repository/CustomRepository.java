package org.go.together.logic.repository;

import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.logic.repository.utils.sql.CustomBuilder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

@Transactional
public abstract class CustomRepository<E extends IdentifiedEntity> {
    private final SessionFactory sessionFactory;

    protected CustomRepository(EntityManagerFactory entityManagerFactory) {
        this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public E save(E entity) {
        return (E) getSession().merge(entity);
    }

    public void delete(E entity) {
        getSession().delete(entity);
    }

    public E findById(UUID uuid) {
        Session session = this.sessionFactory.getCurrentSession();
        return session.get(this.getEntityClass(), uuid);
    }

    public List<E> findAll() {
        return createQuery().fetchAll();
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

    public CustomBuilder<E> createQuery() {
        return new CustomBuilder<>(getEntityClass(), getSession());
    }

    public CustomBuilder.WhereBuilder createWhereClause() {
        return new CustomBuilder.WhereBuilder();
    }
}
