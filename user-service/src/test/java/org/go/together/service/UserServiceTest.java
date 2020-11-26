package org.go.together.service;

import org.go.together.base.Mapper;
import org.go.together.context.RepositoryContext;
import org.go.together.dto.*;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.kafka.producers.CommonCrudProducer;
import org.go.together.kafka.producers.crud.ValidateKafkaProducer;
import org.go.together.model.Interest;
import org.go.together.model.Language;
import org.go.together.model.SystemUser;
import org.go.together.repository.interfaces.InterestRepository;
import org.go.together.repository.interfaces.LanguageRepository;
import org.go.together.service.interfaces.UserService;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = RepositoryContext.class)
class UserServiceTest extends CrudServiceCommonTest<SystemUser, UserDto> {
    @Autowired
    private CommonCrudProducer<GroupLocationDto> locationProducer;

    @Autowired
    private ValidateKafkaProducer<GroupLocationDto> locationValidate;

    @Autowired
    private CommonCrudProducer<GroupPhotoDto> groupPhotoCrud;

    @Autowired
    private ValidateKafkaProducer<GroupPhotoDto> groupPhotoValidate;

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private Mapper<InterestDto, Interest> interestMapper;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private Mapper<LanguageDto, Language> languageMapper;

    @BeforeEach
    public void init() {
        super.init();
        prepareDto(dto);
        prepareDto(updatedDto);
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
                .map(interest -> interestMapper.entityToDto(UUID.randomUUID(), interest))
                .collect(Collectors.toSet());
        userDto.setInterests(interests);
        Set<LanguageDto> languages = userDto.getLanguages().stream()
                .map(languageMapper::dtoToEntity)
                .peek(languageRepository::save)
                .map(language -> languageMapper.entityToDto(UUID.randomUUID(), language))
                .collect(Collectors.toSet());
        userDto.setLanguages(languages);
        return userDto;
    }

    private void prepareDto(UserDto userDto) {
        when(locationValidate.validate(any(UUID.class), eq(userDto.getLocation()))).thenReturn(new ValidationMessageDto(EMPTY));
        when(groupPhotoValidate.validate(any(UUID.class), eq(userDto.getGroupPhoto()))).thenReturn(new ValidationMessageDto(EMPTY));
        when(groupPhotoCrud.update(any(UUID.class), eq(userDto.getGroupPhoto()))).thenReturn(new IdDto(userDto.getGroupPhoto().getId()));
        when(groupPhotoCrud.create(any(UUID.class), eq(userDto.getGroupPhoto()))).thenReturn(new IdDto(userDto.getGroupPhoto().getId()));
        when(locationProducer.create(any(UUID.class), eq(userDto.getLocation()))).thenReturn(new IdDto(userDto.getLocation().getId()));
        when(locationProducer.update(any(UUID.class), eq(userDto.getLocation()))).thenReturn(new IdDto(userDto.getLocation().getId()));
        when(locationProducer.read(any(UUID.class), eq(userDto.getLocation().getId()))).thenReturn(userDto.getLocation());
        when(groupPhotoCrud.read(any(UUID.class), eq(userDto.getGroupPhoto().getId()))).thenReturn(userDto.getGroupPhoto());
    }
}