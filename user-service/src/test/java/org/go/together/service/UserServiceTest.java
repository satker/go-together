package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.IdDto;
import org.go.together.mapper.InterestMapper;
import org.go.together.mapper.LanguageMapper;
import org.go.together.model.SystemUser;
import org.go.together.notification.client.ContentClient;
import org.go.together.notification.client.LocationClient;
import org.go.together.notification.dto.*;
import org.go.together.notification.repository.InterestRepository;
import org.go.together.notification.repository.LanguageRepository;
import org.go.together.repository.exceptions.CannotFindEntityException;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = RepositoryContext.class)
class UserServiceTest extends CrudServiceCommonTest<SystemUser, UserDto> {
    @Autowired
    private LocationClient locationClient;

    @Autowired
    private ContentClient contentClient;

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private InterestMapper interestMapper;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private LanguageMapper languageMapper;

    @BeforeEach
    public void init() {
        super.init();
        prepareDto(dto);
        prepareDto(updatedDto);
    }

    @Test
    public void findUserByLogin() {
        UserDto createdDto = getCreatedEntityId(dto);
        UserDto userByLogin = ((UserService) crudService).findUserByLogin(createdDto.getLogin());

        assertEquals(createdDto, userByLogin);
    }

    @Test
    public void findAuthUserByLogin() {
        UserDto createdDto = getCreatedEntityId(dto);

        AuthUserDto authUserByLogin = ((UserService) crudService).findAuthUserByLogin(createdDto.getLogin());

        assertEquals(createdDto.getId(), authUserByLogin.getId());
        assertEquals(createdDto.getLogin(), authUserByLogin.getLogin());
        assertEquals(createdDto.getRole(), authUserByLogin.getRole());
    }

    @Test
    public void checkIsPresentedMail() {
        UserDto createdDto = getCreatedEntityId(dto);

        boolean isPresentedMail = ((UserService) crudService).checkIsPresentedMail(createdDto.getMail());

        assertTrue(isPresentedMail);
    }

    @Test
    public void checkIsNotPresentedMail() {
        boolean isPresentedMail = ((UserService) crudService).checkIsPresentedMail(factory.manufacturePojo(String.class));

        assertFalse(isPresentedMail);
    }

    @Test
    public void checkIsPresentedUsername() {
        UserDto createdDto = getCreatedEntityId(dto);

        boolean isPresentedUsername = ((UserService) crudService).checkIsPresentedUsername(createdDto.getLogin());

        assertTrue(isPresentedUsername);
    }

    @Test
    public void checkIsNotPresentedUsername() {
        boolean isPresentedUsername = ((UserService) crudService).checkIsPresentedUsername(factory.manufacturePojo(String.class));

        assertFalse(isPresentedUsername);
    }

    @Test
    public void checkLanguages() {
        UserDto createdDto = getCreatedEntityId(dto);

        List<UUID> languages = createdDto.getLanguages().stream()
                .map(LanguageDto::getId)
                .collect(Collectors.toList());
        boolean isCheckedLanguages = ((UserService) crudService).checkLanguages(createdDto.getId(), languages);

        assertTrue(isCheckedLanguages);
    }

    @Test
    public void notCheckLanguages() {
        UserDto createdDto = getCreatedEntityId(dto);

        boolean isCheckedLanguages = ((UserService) crudService).checkLanguages(createdDto.getId(),
                Collections.singletonList(UUID.randomUUID()));

        assertFalse(isCheckedLanguages);
    }

    @Test
    public void getIdLanguagesByOwnerId() {
        UserDto createdDto = getCreatedEntityId(dto);
        Set<UUID> languagesUUID = createdDto.getLanguages().stream().map(LanguageDto::getId).collect(Collectors.toSet());

        Set<UUID> languagesByOwnerId = ((UserService) crudService).getIdLanguagesByOwnerId(createdDto.getId());

        assertTrue(languagesUUID.containsAll(languagesByOwnerId));
    }

    @Test
    public void checkIfUserPresentsById() {
        UserDto createdDto = getCreatedEntityId(dto);

        boolean isUserPresented = ((UserService) crudService).checkIfUserPresentsById(createdDto.getId());

        assertTrue(isUserPresented);
    }

    @Test
    public void checkIfUserNotPresentsById() {
        boolean isUserPresented = ((UserService) crudService).checkIfUserPresentsById(UUID.randomUUID());

        assertFalse(isUserPresented);
    }

    @Test
    public void findSimpleUserDtosByUserIds() {
        UserDto createdDto = getCreatedEntityId(dto);

        Collection<SimpleUserDto> simpleUserDtosByUserIds =
                ((UserService) crudService).findSimpleUserDtosByUserIds(Collections.singleton(createdDto.getId()));

        assertEquals(1, simpleUserDtosByUserIds.size());
        SimpleUserDto simpleUserDto = simpleUserDtosByUserIds.iterator().next();

        assertEquals(createdDto.getLogin(), simpleUserDto.getLogin());
        assertEquals(createdDto.getFirstName(), simpleUserDto.getFirstName());
        assertEquals(createdDto.getLastName(), simpleUserDto.getLastName());
        assertEquals(createdDto.getId(), simpleUserDto.getId());
    }

    @Test
    public void findLoginById() {
        UserDto createdDto = getCreatedEntityId(dto);

        String login = ((UserService) crudService).findLoginById(createdDto.getId());

        assertEquals(createdDto.getLogin(), login);
    }


    @Test
    public void findLoginByNotPresentedId() {
        assertThrows(CannotFindEntityException.class, () -> ((UserService) crudService).findLoginById(UUID.randomUUID()));
    }

    @Override
    protected UserDto createDto() {
        UserDto userDto = factory.manufacturePojo(UserDto.class);
        userDto.setRole(Role.ROLE_USER);
        Set<InterestDto> interests = userDto.getInterests().stream()
                .map(interestMapper::dtoToEntity)
                .peek(interestRepository::save)
                .map(interestMapper::entityToDto)
                .collect(Collectors.toSet());
        userDto.setInterests(interests);
        Set<LanguageDto> languages = userDto.getLanguages().stream()
                .map(languageMapper::dtoToEntity)
                .peek(languageRepository::save)
                .map(languageMapper::entityToDto)
                .collect(Collectors.toSet());
        userDto.setLanguages(languages);
        return userDto;
    }

    private void prepareDto(UserDto userDto) {
        when(locationClient.validateRoute(userDto.getLocation())).thenReturn(EMPTY);
        when(contentClient.validate(userDto.getGroupPhoto())).thenReturn(EMPTY);
        when(contentClient.updateGroup(userDto.getGroupPhoto())).thenReturn(new IdDto(userDto.getGroupPhoto().getId()));
        when(contentClient.createGroup(userDto.getGroupPhoto())).thenReturn(new IdDto(userDto.getGroupPhoto().getId()));
        when(locationClient.createRoute(userDto.getLocation())).thenReturn(new IdDto(userDto.getLocation().getId()));
        when(locationClient.updateRoute(userDto.getLocation())).thenReturn(new IdDto(userDto.getLocation().getId()));
        when(locationClient.getRouteById(userDto.getLocation().getId())).thenReturn(userDto.getLocation());
        when(contentClient.readGroupPhotosById(userDto.getGroupPhoto().getId())).thenReturn(userDto.getGroupPhoto());
    }
}