package org.go.together.service;

import org.go.together.dto.UserDto;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.logic.CrudService;
import org.go.together.mapper.UserMapper;
import org.go.together.model.Language;
import org.go.together.model.SystemUser;
import org.go.together.repository.UserRepository;
import org.go.together.validation.UserValidator;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService extends CrudService<UserDto, SystemUser, UserRepository> {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserService.class);
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, UserMapper userMapper,
                       UserValidator userValidator) {
        super(userRepository, userMapper, userValidator);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDto findUserByLogin(String login) {
        log.debug("user found by login {}", login);
        Optional<SystemUser> userByLogin = userRepository.findUserByLogin(login);
        if (userByLogin.isPresent()) {
            return userMapper.entityToDto(userByLogin.get());
        }
        throw new CannotFindEntityException("Cannot find user by login");
    }

    public boolean checkIsPresentedMail(String mail) {
        log.debug("user found by mail {}", mail);
        return !getUserByMail(mail.replaceAll("\"", "")).isEmpty();
    }

    public boolean checkIsPresentedUsername(String username) {
        log.debug("user found by login {}", username);
        return userRepository.findUserByLogin(username.replaceAll("\"", "")).isPresent();
    }


    public boolean checkLanguages(UUID ownerId, List<UUID> languagesForCompare) {
        if (languagesForCompare.size() != 0) {
            Optional<SystemUser> user = userRepository.findById(ownerId);
            if (user.isPresent()) {
                Set<UUID> userLanguages = user.get().getLanguages().stream()
                        .map(Language::getId)
                        .collect(Collectors.toSet());
                return languagesForCompare.containsAll(userLanguages);
            }
            return false;
        } else {
            return true;
        }
    }

    private Collection<SystemUser> getUserByMail(String mail) {
        return userRepository.findUserByMail(mail);
    }

    public Set<UUID> getIdLanguagesByOwnerId(UUID userId) {
        Optional<SystemUser> user = userRepository.findById(userId);
        return user.map(systemUser -> systemUser.getLanguages().stream()
                .map(Language::getId)
                .collect(Collectors.toSet())).orElse(Collections.emptySet());
    }

/*@Override
    public ImmutableMap<String, FunctionToGetValue> getFields() {
        return null;
    }

    @Override
    public String getServiceName() {
        return "user";
    }*/

}
