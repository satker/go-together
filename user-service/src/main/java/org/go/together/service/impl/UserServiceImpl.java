package org.go.together.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.go.together.base.CommonCrudService;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.*;
import org.go.together.enums.CrudOperation;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.kafka.producers.CrudProducer;
import org.go.together.model.Language;
import org.go.together.model.SystemUser;
import org.go.together.repository.interfaces.UserRepository;
import org.go.together.service.interfaces.EventLikeService;
import org.go.together.service.interfaces.InterestService;
import org.go.together.service.interfaces.LanguageService;
import org.go.together.service.interfaces.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static org.go.together.enums.UserServiceInfo.USERS;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends CommonCrudService<UserDto, SystemUser> implements UserService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CrudProducer<GroupPhotoDto> groupPhotoProducer;
    private final LanguageService languageService;
    private final InterestService interestService;
    private final EventLikeService eventLikeService;
    private final CrudProducer<LocationDto> locationProducer;

    @Override
    public AuthUserDto findAuthUserByLogin(String login) {
        Optional<SystemUser> userByLogin = ((UserRepository) repository).findUserByLogin(login);
        if (userByLogin.isPresent()) {
            SystemUser systemUser = userByLogin.get();
            AuthUserDto build = AuthUserDto.builder()
                    .login(systemUser.getLogin())
                    .password(systemUser.getPassword())
                    .role(systemUser.getRole())
                    .build();
            build.setId(systemUser.getId());
            return build;
        }
        throw new CannotFindEntityException("Cannot find user by login");
    }

    @Override
    public boolean checkIsPresentedMail(String mail) {
        return !((UserRepository) repository).findUserByMail(mail.replaceAll("\"", "")).isEmpty();
    }

    @Override
    public boolean checkIsPresentedUsername(String username) {
        return ((UserRepository) repository).findUserByLogin(username.replaceAll("\"", "")).isPresent();
    }


    @Override
    public boolean checkLanguages(UUID ownerId, List<UUID> languagesForCompare) {
        if (languagesForCompare.size() != 0) {
            Optional<SystemUser> user = repository.findById(ownerId);
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

    @Override
    public Set<UUID> getIdLanguagesByOwnerId(UUID userId) {
        Optional<SystemUser> user = repository.findById(userId);
        return user.map(systemUser -> systemUser.getLanguages().stream()
                .map(Language::getId)
                .collect(Collectors.toSet())).orElse(Collections.emptySet());
    }

    @Override
    public boolean checkIfUserPresentsById(UUID id) {
        return repository.findById(id).isPresent();
    }

    @Override
    protected SystemUser enrichEntity(SystemUser entity, UserDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.UPDATE) {
            Optional<SystemUser> user = updatePassword(entity);

            Role role = user.map(SystemUser::getRole).orElse(Role.ROLE_USER);
            entity.setRole(role);

            asyncEnricher.add("groupPhotoUpdate", () -> updateGroupPhoto(entity, dto));
            asyncEnricher.add("locationUpdate", () -> entity.setLocationId(locationProducer.update(dto.getLocation()).getId()));
        } else if (crudOperation == CrudOperation.CREATE) {
            updatePassword(entity);
            entity.setRole(Role.ROLE_USER);
            createEventLike(entity);
            asyncEnricher.add("locationCreate", () -> entity.setLocationId(locationProducer.create(dto.getLocation()).getId()));
            asyncEnricher.add("groupPhotoCreate", () -> createGroupPhoto(entity, dto));
        } else if (crudOperation == CrudOperation.DELETE) {
            asyncEnricher.add("locationDelete", () -> locationProducer.delete(entity.getLocationId()));
            asyncEnricher.add("groupPhotoDelete", () -> groupPhotoProducer.delete(entity.getGroupPhoto()));
            asyncEnricher.add("eventLikeDelete", () -> eventLikeService.deleteByUserId(entity.getId()));
        }
        return entity;
    }

    private void updateGroupPhoto(SystemUser entity, UserDto dto) {
        GroupPhotoDto groupPhotoDto = dto.getGroupPhoto();
        groupPhotoDto.setGroupId(entity.getId());
        groupPhotoDto.setCategory(PhotoCategory.USER);
        entity.setGroupPhoto(groupPhotoProducer.update(groupPhotoDto).getId());
    }

    private void createGroupPhoto(SystemUser entity, UserDto dto) {
        GroupPhotoDto groupPhotoDto = dto.getGroupPhoto();
        groupPhotoDto.setGroupId(entity.getId());
        groupPhotoDto.setCategory(PhotoCategory.USER);
        entity.setGroupPhoto(groupPhotoProducer.create(groupPhotoDto).getId());
    }

    private void createEventLike(SystemUser entity) {
        EventLikeDto eventLikeDto = new EventLikeDto();
        eventLikeDto.setEventId(entity.getId());
        eventLikeDto.setUsers(Collections.emptySet());
        eventLikeService.create(eventLikeDto);
    }

    private Optional<SystemUser> updatePassword(SystemUser entity) {
        Optional<SystemUser> user = repository.findById(entity.getId());
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

    @Override
    public String getServiceName() {
        return USERS;
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return Map.of(
                "id", FieldMapper.builder()
                        .currentServiceField("id")
                        .fieldClass(UUID.class).build(),
                "login", FieldMapper.builder()
                        .currentServiceField("login")
                        .fieldClass(String.class).build(),
                "languages", FieldMapper.builder()
                        .innerService(languageService)
                        .currentServiceField("languages").build(),
                "interests", FieldMapper.builder()
                        .innerService(interestService)
                        .currentServiceField("interests").build());
    }
}
