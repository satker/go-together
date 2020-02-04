package org.go.together.client;

import org.go.together.dto.IdDto;
import org.go.together.dto.LanguageDto;
import org.go.together.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@FeignClient(name = "user-service", url = "http://localhost:8085")
public interface UserClient {
    @GetMapping("/users/login/{login}")
    UserDto findUserByLogin(@PathVariable("login") String login);

    @PutMapping("/users")
    IdDto add(@RequestBody UserDto input);

    boolean checkIsGoodUsername(String username);

    boolean checkIsGoodMail(String mail);

    boolean checkIsGoodMailForUpdate(String mail, Principal principal);

    IdDto updateValidateUser(@RequestBody UserDto user);

    Set<LanguageDto> getLanguages();

    Set<UUID> getLanguagesByOwnerId(UUID ownerId);

    boolean checkLanguages(UUID ownerId, List<UUID> languagesForCompare);

    IdDto findUserIdByLogin(String login);

    UserDto findById(String id);
}
