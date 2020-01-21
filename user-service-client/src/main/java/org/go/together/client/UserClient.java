package org.go.together.client;

import org.go.together.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8085")
public interface UserClient {
    @GetMapping("/users/login/{login}")
    UserDto findUserByLogin(@PathVariable("login") String login);
}
