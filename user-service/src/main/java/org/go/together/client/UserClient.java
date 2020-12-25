package org.go.together.client;

import org.go.together.base.FindClient;
import org.go.together.dto.IdDto;
import org.go.together.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@FeignClient(name = "user-service")
public interface UserClient extends FindClient {
    @GetMapping("/users/check/login/{login}")
    boolean checkIsGoodUsername(@PathVariable("login") String username);

    @GetMapping("/users/check/mail/{mail}")
    boolean checkIsGoodMail(@PathVariable("mail") String mail);

    @GetMapping("/users/{userId}/check/languages")
    boolean checkLanguages(@PathVariable("userId") UUID userId, @RequestBody List<UUID> languagesForCompare);

    @GetMapping("/users/{userId}/languages")
    Set<UUID> getLanguagesByOwnerId(@PathVariable("userId") UUID userId);

    @GetMapping("/users/{userId}/presents")
    boolean checkIfUserPresentsById(@PathVariable("userId") UUID id);

    @DeleteMapping("/users/likes/{eventId}")
    void deleteEventLike(@PathVariable("eventId") UUID eventId);

    @GetMapping("/users/{userId}/events")
    Set<UUID> getLikedEventsByUserId(@PathVariable("userId") UUID userId);

    @GetMapping("/users/{userId}")
    UserDto readUser(@PathVariable("userId") UUID authorId);

    @PutMapping("/users")
    IdDto createUser(@RequestBody UserDto dto);

    @PostMapping("/users")
    IdDto updateUser(@RequestBody UserDto dto);
}
