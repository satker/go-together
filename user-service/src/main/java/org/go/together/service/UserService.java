package org.go.together.service;

import org.go.together.client.ContentClient;
import org.go.together.dto.IdDto;
import org.go.together.dto.Role;
import org.go.together.dto.SimpleUserDto;
import org.go.together.dto.UserDto;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.logic.CrudService;
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

    public UserService(UserRepository userRepository, UserMapper userMapper,
                       UserValidator userValidator, BCryptPasswordEncoder bCryptPasswordEncoder,
                       ContentClient contentClient,
                       SimpleUserMapper simpleUserMapper) {
        super(userRepository, userMapper, userValidator);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.contentClient = contentClient;
        this.simpleUserMapper = simpleUserMapper;
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

    public boolean checkIfUserPresentsById(UUID id) {
        return userRepository.findById(id).isPresent();
    }

    @Override
    public void updateEntityForCreate(SystemUser entity, UserDto dto) {
        Collection<IdDto> savedPhoto = contentClient.savePhotos(dto.getUserPhotos());
        entity.setPhotoIds(savedPhoto.stream()
                .map(IdDto::getId)
                .collect(Collectors.toSet()));
        entity.setPassword(bCryptPasswordEncoder.encode(entity.getPassword()));
        entity.setRole(Role.ROLE_USER);
    }

    @Override
    public void updateEntityForUpdate(SystemUser entity, UserDto dto) {
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
    }

    @Override
    protected void actionsBeforeDelete(SystemUser entity) {
        contentClient.deletePhotoById(entity.getPhotoIds());
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

    public Set<SimpleUserDto> getUsersLoginLikedEventId(UUID eventId) {
        return userRepository.findUsersLoginLikedEventId(eventId).stream()
                .map(simpleUserMapper::entityToDto)
                .collect(Collectors.toSet());
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

/*@Override
    public ImmutableMap<String, FunctionToGetValue> getFields() {
        return null;
    }

    @Override
    public String getServiceName() {
        return "user";
    }*/

}
