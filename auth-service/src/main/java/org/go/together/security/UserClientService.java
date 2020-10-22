package org.go.together.security;

import org.go.together.client.UserClient;
import org.go.together.dto.AuthUserDto;
import org.springframework.stereotype.Service;

@Service
public class UserClientService {
    private final UserClient userClient;

    public UserClientService(UserClient userClient) {
        this.userClient = userClient;
    }

    public AuthUserDto findAuthUserByLogin(String userName) {
        return userClient.findAuthUserByLogin(userName);
    }
}
