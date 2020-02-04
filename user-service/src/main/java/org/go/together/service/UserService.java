package org.go.together.service;

import org.go.together.dto.IdDto;
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
        Optional<SystemUser> userByLogin = getUserByLogin(login);
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
        return getUserByLogin(username.replaceAll("\"", "")).isPresent();
    }

    public boolean checkIsGoodMailForUpdate(String userMail, String mail) {
        UserDto currentUser = findUserByLogin(userMail);
        if (currentUser.getMail().equals(mail)) {
            return true;
        } else return !getUserByMail(mail).isEmpty();
    }


    public boolean checkLanguages(UUID ownerId, List<UUID> languagesForCompare) {
        if (languagesForCompare.size() != 0) {
            Set<UUID> userLanguages = userRepository.findById(ownerId).getLanguages().stream()
                    .map(Language::getId)
                    .collect(Collectors.toSet());
            return languagesForCompare.containsAll(userLanguages);
        } else {
            return true;
        }
    }

    public IdDto findUserIdByLogin(String login) {
        Optional<SystemUser> userByLogin = getUserByLogin(login);
        if (userByLogin.isPresent()) {
            return new IdDto(userByLogin.get().getId());
        }
        throw new CannotFindEntityException("Cannot find user by login");
    }

    private Collection<SystemUser> getUserByMail(String mail) {
        return userRepository.findUserByMail(mail);
    }

    private Optional<SystemUser> getUserByLogin(String login) {
        return userRepository.findUserByLogin(login);
        /*if (userByLogin.isPresent()){
            return userByLogin.get();
        }
        throw new CannotFindEntityException("Cannot find user by login");*/
    }

    public Set<UUID> getIdLanguagesByOwnerId(UUID ownerId) {
        return userRepository.findById(ownerId).getLanguages().stream()
                .map(Language::getId)
                .collect(Collectors.toSet());
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
