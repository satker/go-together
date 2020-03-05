package org.go.together.repository;

import org.go.together.logic.repository.CustomRepository;
import org.go.together.logic.repository.utils.sql.SqlOperator;
import org.go.together.model.SystemUser;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
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
                .where(createWhere().condition("mail", SqlOperator.LIKE, mail))
                .fetchAll();
    }

    @Transactional
    public Collection<SystemUser> findUsersLoginLikedEventId(UUID eventId) {
        return createQuery().where(createWhere().condition("eventLikeIds", SqlOperator.EQUAL, eventId))
                .fetchAll();
    }
}
