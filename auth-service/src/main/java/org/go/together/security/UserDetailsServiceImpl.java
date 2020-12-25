package org.go.together.security;

import org.go.together.dto.AuthUserDto;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserClientService userClientService;

    public UserDetailsServiceImpl(UserClientService userClientService) {
        this.userClientService = userClientService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AuthUserDto user = userClientService.findAuthUserByLogin(username);
        return Optional.ofNullable(user).map(this::getUser).orElseThrow(() ->
                new UsernameNotFoundException("Username: " + username + " not found"));
    }

    private User getUser(AuthUserDto user) {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole().name());
        return new User(user.getId().toString(), user.getPassword(), Set.of(simpleGrantedAuthority));
    }
}