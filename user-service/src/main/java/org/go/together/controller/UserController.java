package org.go.together.controller;

import org.go.together.client.UserClient;
import org.go.together.dto.IdDto;
import org.go.together.dto.InterestDto;
import org.go.together.dto.LanguageDto;
import org.go.together.dto.UserDto;
import org.go.together.service.InterestService;
import org.go.together.service.LanguageService;
import org.go.together.service.UserService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
class UserController implements UserClient {
    private final UserService userService;
    private final LanguageService languageService;
    private final InterestService interestService;

    public UserController(UserService userService,
                          LanguageService languageService,
                          InterestService interestService) {
        this.userService = userService;
        this.languageService = languageService;
        this.interestService = interestService;
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
    public Set<LanguageDto> getLanguages() {
        return languageService.getLanguages();
    }

    @Override
    public Collection<InterestDto> getInterests() {
        return interestService.getInterests();
    }

    @Override
    public Set<UUID> getLanguagesByOwnerId(UUID userId) {
        return userService.getIdLanguagesByOwnerId(userId);
    }

    @Override
    public boolean checkLanguages(UUID ownerId, List<UUID> languagesForCompare) {
        return userService.checkLanguages(ownerId, languagesForCompare);
    }

    @Override
    public IdDto updateUser(UserDto user) {
        return userService.update(user);
    }

    @Override
    public UserDto findUserByLogin(String login) {
        return userService.findUserByLogin(login);
    }

    @Override
    public UserDto findById(UUID id) {
        return userService.read(id);
    }

    @Override
    public boolean checkIfUserPresentsById(UUID id) {
        return userService.checkIfUserPresentsById(id);
    }
}
