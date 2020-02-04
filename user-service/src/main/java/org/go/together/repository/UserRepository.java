package org.go.together.repository;

import org.go.together.logic.repository.CustomRepository;
import org.go.together.logic.repository.utils.sql.CustomBuilder;
import org.go.together.logic.repository.utils.sql.SqlOperator;
import org.go.together.model.SystemUser;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository extends CustomRepository<SystemUser> {
    protected UserRepository(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public Optional<SystemUser> findUserByLogin(String login) {
        SystemUser systemUser = new SystemUser();
        systemUser.setId(UUID.randomUUID());
        systemUser.setDescription("dssds");
        systemUser.setFirstName("art");
        systemUser.setLastName("kun");
        systemUser.setLocationId(UUID.randomUUID());
        systemUser.setLogin("kkk");
        systemUser.setPassword("13693509");
        systemUser.setMail("kun@mail.ru");
        systemUser.setPhotoId(UUID.randomUUID());
        systemUser.setRole("ROLE_USER");
        save(systemUser);
        CustomBuilder.WhereBuilder where = createWhereClause().condition("login", SqlOperator.EQUAL, login);
        return createQuery().where(where).fetchOne();
    }

    public List<SystemUser> findUserByMail(String mail) {
        CustomBuilder.WhereBuilder where = createWhereClause().condition("mail", SqlOperator.LIKE, mail);
        return createQuery().where(where).fetchAll();
    }
}
