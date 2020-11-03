package org.go.together.controller;

import lombok.RequiredArgsConstructor;
import org.go.together.base.impl.FindController;
import org.go.together.client.UserClient;
import org.go.together.dto.*;
import org.go.together.dto.form.FormDto;
import org.go.together.service.interfaces.EventLikeService;
import org.go.together.service.interfaces.UserService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
class UserController extends FindController implements UserClient {
    private final UserService userService;
    private final EventLikeService eventLikeService;

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
}
