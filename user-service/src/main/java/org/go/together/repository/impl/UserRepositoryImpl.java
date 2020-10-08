package org.go.together.repository.impl;

import org.go.together.model.SystemUser;
import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.interfaces.UserRepository;
import org.go.together.repository.sql.SqlOperator;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public class UserRepositoryImpl extends CustomRepositoryImpl<SystemUser> implements UserRepository {
    @Override
    public Optional<SystemUser> findUserByLogin(String login) {
        return createQuery()
                .where(createWhere().condition("login", SqlOperator.EQUAL, login))
                .fetchOne();
    }

    @Override
    public Collection<SystemUser> findUserByMail(String mail) {
        return createQuery()
                .where(createWhere().condition("mail", SqlOperator.EQUAL, mail))
                .fetchAll();
    }

    @Override
    public Collection<SystemUser> findAllByIds(Set<UUID> userIds) {
        return createQuery().where(createWhere().condition("id", SqlOperator.IN, userIds))
                .fetchAll();
    }
}
