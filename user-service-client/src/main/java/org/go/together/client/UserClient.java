package org.go.together.client;

import org.go.together.dto.IdDto;
import org.go.together.dto.InterestDto;
import org.go.together.dto.LanguageDto;
import org.go.together.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@FeignClient(name = "user-service", url = "http://localhost:8085")
public interface UserClient {
    @GetMapping("/users/login/{login}")
    UserDto findUserByLogin(@PathVariable("login") String login);

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

    @GetMapping("/users/languages")
    Set<LanguageDto> getLanguages();

    @GetMapping("/users/interests")
    Collection<InterestDto> getInterests();

    @GetMapping("/users/{userId}/languages")
    Set<UUID> getLanguagesByOwnerId(@PathVariable("userId") UUID userId);

    @GetMapping("/users/{userId}")
    UserDto findById(@PathVariable("userId") UUID id);

    @DeleteMapping("/users/{userId}")
    void deleteUserById(@PathVariable("userId") UUID id);

    @GetMapping("/users/{userId}/presents")
    boolean checkIfUserPresentsById(@PathVariable("userId") UUID id);
}
