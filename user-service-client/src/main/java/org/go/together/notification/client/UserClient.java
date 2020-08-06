package org.go.together.notification.client;

import org.go.together.dto.IdDto;
import org.go.together.find.client.FindClient;
import org.go.together.notification.dto.AuthUserDto;
import org.go.together.notification.dto.EventLikeDto;
import org.go.together.notification.dto.SimpleUserDto;
import org.go.together.notification.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@FeignClient(name = "user-service")
public interface UserClient extends FindClient {
    @GetMapping("/users")
    UserDto findUserByLogin(@RequestParam("login") String login);

    @GetMapping("/users/auth")
    AuthUserDto findAuthUserByLogin(@RequestParam("login") String login);

    @PostMapping("/users/simple")
    Collection<SimpleUserDto> findSimpleUserDtosByUserIds(@RequestBody Set<UUID> userIds);

    @PutMapping("/users")
    IdDto add(@RequestBody UserDto input);

    @GetMapping("/users/check/login/{login}")
    boolean checkIsGoodUsername(@PathVariable("login") String username);

    @GetMapping("/users/check/mail/{mail}")
    boolean checkIsGoodMail(@PathVariable("mail") String mail);

    @GetMapping("/users/{userId}/check/languages")
    boolean checkLanguages(@PathVariable("userId") UUID userId, @RequestBody List<UUID> languagesForCompare);

    @PostMapping("/users")
    IdDto updateUser(@RequestBody UserDto user);

    @GetMapping("/languages")
    Collection<Object> getLanguages();

    @GetMapping("/interests")
    Collection<Object> getInterests();

    @GetMapping("/users/{userId}/languages")
    Set<UUID> getLanguagesByOwnerId(@PathVariable("userId") UUID userId);

    @GetMapping("/users/{userId}")
    UserDto findById(@PathVariable("userId") UUID id);

    @GetMapping("/users/{userId}/login")
    String findLoginById(@PathVariable("userId") UUID id);

    @DeleteMapping("/users/{userId}")
    void deleteUserById(@PathVariable("userId") UUID id);

    @GetMapping("/users/{userId}/presents")
    boolean checkIfUserPresentsById(@PathVariable("userId") UUID id);

    @PutMapping("/users/likes")
    IdDto createEventLike(@RequestBody EventLikeDto eventLikeDto);

    @PostMapping("/users/likes")
    IdDto updateEventLike(@RequestBody EventLikeDto eventLikeDto);

    @DeleteMapping("/users/likes/{eventId}")
    void deleteEventLike(@PathVariable("eventId") UUID eventId);

    @GetMapping("/users/{userId}/events")
    Set<UUID> getLikedEventsByUserId(@PathVariable("userId") UUID userId);

    @PostMapping("/events/likes")
    Set<EventLikeDto> getUsersLikedEventIds(@RequestBody Set<UUID> eventIds);
}
