package org.go.together.service;

import com.google.common.collect.ImmutableMap;
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
import java.util.function.Function;
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

    public boolean checkIfUserPresentsById(UUID id) {
        return userRepository.findById(id).isPresent();
    }

    @Override
    protected void enrichEntity(SystemUser entity, UserDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.UPDATE) {
            Collection<UUID> previousPhotos = userRepository.findById(entity.getId())
                    .map(SystemUser::getPhotoIds)
                    .orElse(Collections.emptySet());
            Role role = userRepository.findById(entity.getId()).map(SystemUser::getRole).orElse(Role.ROLE_USER);
            contentClient.deletePhotoById(previousPhotos);
            Collection<IdDto> savedPhoto = contentClient.savePhotos(dto.getUserPhotos());
            entity.setPhotoIds(savedPhoto.stream()
                    .map(IdDto::getId)
                    .collect(Collectors.toSet()));
            entity.setPassword(bCryptPasswordEncoder.encode(entity.getPassword()));
            entity.setRole(role);
        } else if (crudOperation == CrudOperation.CREATE) {
            Collection<IdDto> savedPhoto = contentClient.savePhotos(dto.getUserPhotos());
            entity.setPhotoIds(savedPhoto.stream()
                    .map(IdDto::getId)
                    .collect(Collectors.toSet()));
            entity.setPassword(bCryptPasswordEncoder.encode(entity.getPassword()));
            entity.setRole(Role.ROLE_USER);
        } else if (crudOperation == CrudOperation.DELETE) {
            contentClient.deletePhotoById(entity.getPhotoIds());
        }
    }

    public Set<UUID> getLikedEventsByUserId(UUID userId) {
        Optional<SystemUser> byId = userRepository.findById(userId);
        if (byId.isPresent()) {
            return byId.get().getEventLikeIds();
        }
        return Collections.emptySet();
    }

    public Set<UUID> deleteLikedEventsByUserId(UUID userId, Set<UUID> eventIds) {
        Optional<SystemUser> byId = userRepository.findById(userId);
        if (byId.isPresent()) {
            SystemUser user = byId.get();
            user.getEventLikeIds().removeAll(eventIds);
            return userRepository.save(user).getEventLikeIds();
        }
        return Collections.emptySet();
    }

    public Map<UUID, Collection<SimpleUserDto>> getUsersLikedEventIds(Set<UUID> eventIds) {
        return eventIds.stream().collect(Collectors.toMap(Function.identity(),
                eventId -> simpleUserMapper.entitiesToDtos(userRepository.findUsersLoginLikedEventId(eventId))));
    }

    public boolean saveLikedEventByUserId(UUID userId, UUID eventId) {
        Optional<SystemUser> byId = userRepository.findById(userId);
        if (byId.isPresent()) {
            SystemUser user = byId.get();
            Set<UUID> eventLikeIds = user.getEventLikeIds();
            if (eventLikeIds.stream().anyMatch(eventLike -> eventLike.equals(eventId))) {
                eventLikeIds.remove(eventId);
                Set<UUID> newUserLikes = userRepository.save(user).getEventLikeIds();
                return newUserLikes.stream().anyMatch(userLikeEvent -> userLikeEvent.equals(eventId));
            } else {
                eventLikeIds.add(eventId);
                Set<UUID> newUserLikes = userRepository.save(user).getEventLikeIds();
                return newUserLikes.stream().anyMatch(userLikeEvent -> userLikeEvent.equals(eventId));
            }
        }
        return false;
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
