package org.go.together.security;

import org.go.together.client.UserClient;
import org.go.together.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserClient userClient;

    public UserDetailsServiceImpl(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDto user = userClient.findUserByLogin(username);
        if (user != null) {
            GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
            return new User(user.getLogin(),
                    user.getPassword(), Collections.singletonList(authority));
        }
        // If user not found. Throw this exception.
        throw new UsernameNotFoundException("Username: " + username + " not found");
    }
}