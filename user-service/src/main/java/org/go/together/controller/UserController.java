package org.go.together.controller;

import org.go.together.client.UserClient;
import org.go.together.dto.*;
import org.go.together.dto.filter.FormDto;
import org.go.together.logic.controllers.FindController;
import org.go.together.service.EventLikeService;
import org.go.together.service.InterestService;
import org.go.together.service.LanguageService;
import org.go.together.service.UserService;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
class UserController extends FindController implements UserClient {
    private final UserService userService;
    private final LanguageService languageService;
    private final InterestService interestService;
    private final EventLikeService eventLikeService;

    public UserController(UserService userService,
                          LanguageService languageService,
                          InterestService interestService,
                          EventLikeService eventLikeService) {
        super(Arrays.asList(userService, languageService, interestService, eventLikeService));
        this.userService = userService;
        this.languageService = languageService;
        this.interestService = interestService;
        this.eventLikeService = eventLikeService;
    }

    @Override
    public ResponseDto<Object> find(FormDto formDto) {
        return super.find(formDto);
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
    public AuthUserDto findAuthUserByLogin(String login) {
        return userService.findAuthUserByLogin(login);
    }

    @Override
    public Collection<SimpleUserDto> findSimpleUserDtosByUserIds(Set<UUID> userIds) {
        return userService.findSimpleUserDtosByUserIds(userIds);
    }

    @Override
    public UserDto findById(UUID id) {
        return userService.read(id);
    }

    @Override
    public String findLoginById(UUID id) {
        return userService.findLoginById(id);
    }

    @Override
    public void deleteUserById(UUID id) {
        userService.delete(id);
    }

    @Override
    public boolean checkIfUserPresentsById(UUID id) {
        return userService.checkIfUserPresentsById(id);
    }

    @Override
    public IdDto createEventLike(EventLikeDto eventLikeDto) {
        return eventLikeService.create(eventLikeDto);
    }

    @Override
    public IdDto updateEventLike(EventLikeDto eventLikeDto) {
        return eventLikeService.update(eventLikeDto);
    }

    @Override
    public void deleteEventLike(UUID eventId) {
        eventLikeService.deleteByEventId(eventId);
    }

    @Override
    public Set<UUID> getLikedEventsByUserId(UUID userId) {
        // FROM USER
        return eventLikeService.findLikedEventIdsByUserId(userId);
    }

    @Override
    public Set<EventLikeDto> getUsersLikedEventIds(Set<UUID> eventIds) {
        return eventLikeService.findUsersLikedEventIds(eventIds);
    }
}
