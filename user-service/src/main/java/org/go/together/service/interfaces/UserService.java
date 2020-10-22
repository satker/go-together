package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.dto.AuthUserDto;
import org.go.together.dto.SimpleUserDto;
import org.go.together.dto.UserDto;
import org.go.together.find.FindService;
import org.go.together.model.SystemUser;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserService extends CrudService<UserDto>, FindService<SystemUser> {
    String findLoginById(UUID id);

    Collection<SimpleUserDto> findSimpleUserDtosByUserIds(Set<UUID> userIds);

    boolean checkIfUserPresentsById(UUID id);

    Set<UUID> getIdLanguagesByOwnerId(UUID userId);

    boolean checkLanguages(UUID ownerId, List<UUID> languagesForCompare);

    boolean checkIsPresentedUsername(String username);

    boolean checkIsPresentedMail(String mail);

    AuthUserDto findAuthUserByLogin(String login);
}
