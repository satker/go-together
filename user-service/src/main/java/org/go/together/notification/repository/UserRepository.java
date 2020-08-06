package org.go.together.notification.repository;

import org.go.together.model.SystemUser;
import org.go.together.repository.CustomRepository;
import org.go.together.repository.sql.SqlOperator;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public class UserRepository extends CustomRepository<SystemUser> {
    @Transactional
    public Optional<SystemUser> findUserByLogin(String login) {
        return createQuery()
                .where(createWhere().condition("login", SqlOperator.EQUAL, login))
                .fetchOne();
    }

    @Transactional
    public Collection<SystemUser> findUserByMail(String mail) {
        return createQuery()
                .where(createWhere().condition("mail", SqlOperator.EQUAL, mail))
                .fetchAll();
    }

    @Transactional
    public Collection<SystemUser> findAllByIds(Set<UUID> userIds) {
        return createQuery().where(createWhere().condition("id", SqlOperator.IN, userIds))
                .fetchAll();
    }
}
