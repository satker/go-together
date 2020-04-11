package org.go.together.client;

import org.go.together.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@FeignClient(name = "user-service", url = "http://localhost:8085")
public interface UserClient {
    @GetMapping("/users")
    UserDto findUserByLogin(@RequestParam("login") String login);

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
    Set<LanguageDto> getLanguages();

    @GetMapping("/interests")
    Collection<InterestDto> getInterests();

    @GetMapping("/users/{userId}/languages")
    Set<UUID> getLanguagesByOwnerId(@PathVariable("userId") UUID userId);

    @GetMapping("/users/{userId}")
    UserDto findById(@PathVariable("userId") UUID id);

    @DeleteMapping("/users/{userId}")
    void deleteUserById(@PathVariable("userId") UUID id);

    @GetMapping("/users/{userId}/presents")
    boolean checkIfUserPresentsById(@PathVariable("userId") UUID id);

    @PutMapping("/users/{userId}/events/{eventId}")
    boolean saveLikedEventsByUserId(@PathVariable("userId") UUID userId,
                                    @PathVariable("eventId") UUID eventId);

    @GetMapping("/users/{userId}/events")
    Set<UUID> getLikedEventsByUserId(@PathVariable("userId") UUID userId);

    @DeleteMapping("/users/{userId}/events")
    Set<UUID> deleteLikedEventsByUserId(@PathVariable("userId") UUID userId,
                                        @RequestBody Set<UUID> eventIds);

    @GetMapping("/events/{eventId}/likes")
    Set<SimpleUserDto> getUsersLoginLikedEventId(@PathVariable("eventId") UUID eventId);
}
