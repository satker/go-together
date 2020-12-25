package org.go.together.repository.interfaces;

import org.go.together.base.CustomRepository;
import org.go.together.model.SystemUser;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends CustomRepository<SystemUser> {
    Optional<SystemUser> findUserByLogin(String login);
    Collection<SystemUser> findUserByMail(String mail);
    Collection<SystemUser> findAllByIds(Set<UUID> userIds);
}
