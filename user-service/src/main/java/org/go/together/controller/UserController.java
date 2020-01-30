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

    public IdDto add(UserDto input) {
        return userService.create(input);
    }

    public boolean checkIsGoodUsername(String username) {
        return userService.checkIsPresentedUsername(username);
    }

    public boolean checkIsGoodMail(String mail) {
        return userService.checkIsPresentedMail(mail);
    }

    public boolean checkIsGoodMailForUpdate(String mail, Principal principal) {
        return userService.checkIsGoodMailForUpdate(principal.getName(), mail);
    }

    public IdDto updateValidateUser(UserDto user) {
        return userService.update(user);
    }

    public Set<LanguageDto> getLanguages() {
        return languageService.getLanguages();
    }

    public Set<String> getLanguagesByOwnerId(String ownerId) {
        return userService.getIdLanguagesByOwnerId(ownerId);
    }

    public boolean checkLanguages(String ownerId, List<String> languagesForCompare) {
        return userService.checkLanguages(ownerId, languagesForCompare);
    }

    @Override
    public UserDto findUserByLogin(String login) {
        return userService.findUserByLogin(login);
    }

    public IdDto findUserIdByLogin(String login) {
        return userService.findUserIdByLogin(login);
    }

    public UserDto findById(String id) {
        return userService.read(UUID.fromString(id));
    }
}
