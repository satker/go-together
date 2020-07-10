package org.go.together.service;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.go.together.client.ContentClient;
import org.go.together.dto.*;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.enums.CrudOperation;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.logic.services.CrudService;
import org.go.together.mapper.SimpleUserMapper;
import org.go.together.mapper.UserMapper;
import org.go.together.model.Language;
import org.go.together.model.SystemUser;
import org.go.together.repository.UserRepository;
import org.go.together.validation.UserValidator;
import org.slf4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService extends CrudService<UserDto, SystemUser> {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserService.class);
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ContentClient contentClient;
    private final SimpleUserMapper simpleUserMapper;
    private final LanguageService languageService;
    private final InterestService interestService;

    public UserService(UserRepository userRepository, UserMapper userMapper,
                       UserValidator userValidator, BCryptPasswordEncoder bCryptPasswordEncoder,
                       ContentClient contentClient,
                       SimpleUserMapper simpleUserMapper,
                       LanguageService languageService,
                       InterestService interestService) {
        super(userRepository, userMapper, userValidator);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.contentClient = contentClient;
        this.simpleUserMapper = simpleUserMapper;
        this.languageService = languageService;
        this.interestService = interestService;
    }

    public UserDto findUserByLogin(String login) {
        log.debug("user found by login {}", login);
        Optional<SystemUser> userByLogin = userRepository.findUserByLogin(login);
        if (userByLogin.isPresent()) {
            return userMapper.entityToDto(userByLogin.get());
        }
        throw new CannotFindEntityException("Cannot find user by login");
    }

    public AuthUserDto findAuthUserByLogin(String login) {
        log.debug("auth user found by login {}", login);
        Optional<SystemUser> userByLogin = userRepository.findUserByLogin(login);
        if (userByLogin.isPresent()) {
            SystemUser systemUser = userByLogin.get();
            return AuthUserDto.builder()
                    .id(systemUser.getId())
                    .login(systemUser.getLogin())
                    .password(systemUser.getPassword())
                    .role(systemUser.getRole())
                    .build();
        }
        throw new CannotFindEntityException("Cannot find user by login");
    }

    public boolean checkIsPresentedMail(String mail) {
        log.debug("user found by mail {}", mail);
        return !userRepository.findUserByMail(mail.replaceAll("\"", "")).isEmpty();
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

    public Set<UUID> getIdLanguagesByOwnerId(UUID userId) {
        Optional<SystemUser> user = userRepository.findById(userId);
        return user.map(systemUser -> systemUser.getLanguages().stream()
                .map(Language::getId)
                .collect(Collectors.toSet())).orElse(Collections.emptySet());
    }

    public boolean checkIfUserPresentsById(UUID id) {
        return userRepository.findById(id).isPresent();
    }

    @Override
    protected SystemUser enrichEntity(SystemUser entity, UserDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.UPDATE) {
            Optional<SystemUser> user = updatePassword(entity);

            Role role = user.map(SystemUser::getRole).orElse(Role.ROLE_USER);

            GroupPhotoDto groupPhotoDto = dto.getGroupPhoto();
            groupPhotoDto.setGroupId(entity.getId());
            groupPhotoDto.setCategory(PhotoCategory.USER);
            IdDto groupPhotoId = contentClient.updateGroup(groupPhotoDto);
            entity.setGroupPhoto(groupPhotoId.getId());

            entity.setRole(role);
        } else if (crudOperation == CrudOperation.CREATE) {
            updatePassword(entity);

            GroupPhotoDto groupPhotoDto = dto.getGroupPhoto();
            groupPhotoDto.setGroupId(entity.getId());
            groupPhotoDto.setCategory(PhotoCategory.USER);
            IdDto groupPhotoId = contentClient.createGroup(groupPhotoDto);
            entity.setGroupPhoto(groupPhotoId.getId());
            entity.setId(entity.getId());
            entity.setRole(Role.ROLE_USER);
        } else if (crudOperation == CrudOperation.DELETE) {
            contentClient.delete(entity.getGroupPhoto());
        }
        return entity;
    }

    private Optional<SystemUser> updatePassword(SystemUser entity) {
        Optional<SystemUser> user = userRepository.findById(entity.getId());
        String password = entity.getPassword();
        if (StringUtils.isNotBlank(password)) {
            entity.setPassword(bCryptPasswordEncoder.encode(password));
        } else {
            String passwordFromDB = user.map(SystemUser::getPassword)
                    .orElseThrow(() -> new CannotFindEntityException("Cannot find user in database"));
            entity.setPassword(passwordFromDB);
        }
        return user;
    }

    public Collection<SimpleUserDto> findSimpleUserDtosByUserIds(Set<UUID> userIds) {
        Collection<SystemUser> allUsersByIds = userRepository.findAllByIds(userIds);
        return simpleUserMapper.entitiesToDtos(allUsersByIds);
    }

    @Override
    public String getServiceName() {
        return "user";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return ImmutableMap.<String, FieldMapper>builder()
                .put("languages", FieldMapper.builder()
                        .innerService(languageService)
                        .currentServiceField("languages").build())
                .put("interests", FieldMapper.builder()
                        .innerService(interestService)
                        .currentServiceField("interests").build())
                .build();
    }

    public String findLoginById(UUID id) {
        log.debug("user found by id {}", id);
        Optional<SystemUser> userById = userRepository.findById(id);
        if (userById.isPresent()) {
            return userById.get().getLogin();
        }
        throw new CannotFindEntityException("Cannot find user by login");
    }
}
