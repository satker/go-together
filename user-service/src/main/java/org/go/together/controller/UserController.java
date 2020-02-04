package org.go.together.controller;

import org.go.together.client.UserClient;
import org.go.together.dto.IdDto;
import org.go.together.dto.LanguageDto;
import org.go.together.dto.UserDto;
import org.go.together.service.LanguageService;
import org.go.together.service.UserService;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
class UserController implements UserClient {
    private final UserService userService;
    private final LanguageService languageService;

    public UserController(UserService userService, LanguageService languageService) {
        this.userService = userService;
        this.languageService = languageService;
    }

    @Override
    public IdDto add(UserDto input) {
        return userService.create(input);
    }

    @Override
    public boolean checkIsGoodUsername(String username) {
        return userService.checkIsPresentedUsername(username);
    }

    @Override
    public boolean checkIsGoodMail(String mail) {
        return userService.checkIsPresentedMail(mail);
    }

    @Override
    public boolean checkIsGoodMailForUpdate(String mail, Principal principal) {
        return userService.checkIsGoodMailForUpdate(principal.getName(), mail);
    }

    @Override
    public IdDto updateValidateUser(UserDto user) {
        return userService.update(user);
    }

    @Override
    public Set<LanguageDto> getLanguages() {
        return languageService.getLanguages();
    }

    @Override
    public Set<UUID> getLanguagesByOwnerId(UUID ownerId) {
        return userService.getIdLanguagesByOwnerId(ownerId);
    }

    @Override
    public boolean checkLanguages(UUID ownerId, List<UUID> languagesForCompare) {
        return userService.checkLanguages(ownerId, languagesForCompare);
    }

    @Override
    public UserDto findUserByLogin(String login) {
        return userService.findUserByLogin(login);
    }

    @Override
    public IdDto findUserIdByLogin(String login) {
        return userService.findUserIdByLogin(login);
    }

    @Override
    public UserDto findById(String id) {
        return userService.read(UUID.fromString(id));
    }
}
