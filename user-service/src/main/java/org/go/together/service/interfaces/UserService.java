package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.base.FindService;
import org.go.together.dto.AuthUserDto;
import org.go.together.dto.UserDto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserService extends CrudService<UserDto>, FindService<UserDto> {
    String findLoginById(UUID id);

    boolean checkIfUserPresentsById(UUID id);

    Set<UUID> getIdLanguagesByOwnerId(UUID userId);

    boolean checkLanguages(UUID ownerId, List<UUID> languagesForCompare);

    boolean checkIsPresentedUsername(String username);

    boolean checkIsPresentedMail(String mail);

    AuthUserDto findAuthUserByLogin(String login);
}
