package org.go.together.service;

import org.go.together.dto.IdDto;
import org.go.together.dto.UserDto;
import org.go.together.logic.CrudService;
import org.go.together.mapper.UserMapper;
import org.go.together.repository.UserRepository;
import org.go.together.repository.tables.records.SystemUserRecord;
import org.go.together.validation.UserValidator;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService extends CrudService<UserDto, SystemUserRecord, UserRepository> {
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
        userRepository.findById(UUID.randomUUID().toString());
        //Optional<User> userByLogin = Optional.ofNullable(getUserByLogin(login));
        return
                /*userByLogin.map(userMapper::entityToDto).orElse(null)*/
                null;
    }

    public boolean checkIsPresentedMail(String mail) {
        log.debug("user found by mail {}", mail);
        return !getUserByMail(mail.replaceAll("\"", "")).isEmpty();
    }

    public boolean checkIsPresentedUsername(String username) {
        log.debug("user found by login {}", username);
        return getUserByLogin(username.replaceAll("\"", "")) != null;
    }

    public boolean checkIsGoodMailForUpdate(String userMail, String mail) {
        UserDto currentUser = findUserByLogin(userMail);
        if (currentUser.getMail().equals(mail)) {
            return true;
        } else return !getUserByMail(mail).isEmpty();
    }


    public boolean checkLanguages(String ownerId, List<String> languagesForCompare) {
        if (languagesForCompare.size() != 0) {
            return true;
            //return languagesForCompare.containsAll(super.findAll().get(ownerId).getLanguagesId());
        } else {
            return true;
        }
    }

    public IdDto findUserIdByLogin(String login) {
        return new IdDto(
                /*getUserByLogin(login).getId()*/
                null);
    }

    private Collection<SystemUserRecord> getUserByMail(String mail) {
        return null;
        //return super.find(user -> user.getMail().matches(".*" + mail + ".*"));
    }

    private SystemUserRecord getUserByLogin(String login) {
        return null;
        //return super.find(user -> user.getLogin().toLowerCase().equals(login.toLowerCase())).iterator().next();
    }

    public Set<String> getIdLanguagesByOwnerId(String ownerId) {
        return null;
        //return super.find(user -> user.getId().equals(ownerId)).iterator().next().getLanguagesId();
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
