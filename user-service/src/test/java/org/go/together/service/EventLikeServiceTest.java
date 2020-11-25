package org.go.together.service;

import org.go.together.base.Mapper;
import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.context.RepositoryContext;
import org.go.together.dto.*;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.kafka.NotificationEvent;
import org.go.together.model.EventLike;
import org.go.together.model.Interest;
import org.go.together.model.Language;
import org.go.together.model.SystemUser;
import org.go.together.repository.interfaces.EventLikeRepository;
import org.go.together.repository.interfaces.InterestRepository;
import org.go.together.repository.interfaces.LanguageRepository;
import org.go.together.repository.interfaces.UserRepository;
import org.go.together.service.interfaces.EventLikeService;
import org.go.together.service.interfaces.UserService;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = RepositoryContext.class)
class EventLikeServiceTest extends CrudServiceCommonTest<EventLike, EventLikeDto> {
    @Autowired
    private ContentClient contentClient;

    @Autowired
    private EventLikeRepository eventLikeRepository;

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private Mapper<InterestDto, Interest> interestMapper;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private Mapper<LanguageDto, Language> languageMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private Mapper<EventLikeDto, EventLike> eventLikeMapper;

    @Autowired
    private LocationClient locationClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaTemplate<UUID, NotificationEvent> kafkaTemplate;

    @BeforeEach
    public void init() {
        super.init();
        doNothing().when(kafkaTemplate.send(any(), any(), any()));
        updatedDto.setEventId(dto.getEventId());
    }

    @Test
    public void findLikedEventIdsByUserId() {
        EventLikeDto createdDto = getCreatedEntityId(dto);
        EventLike eventLike = getEventLikeWithUser(createdDto);

        Set<UUID> likedEvents =
                ((EventLikeService) crudService).findLikedEventIdsByUserId(eventLike.getUsers().iterator().next().getId());

        assertEquals(1, likedEvents.size());
        assertEquals(eventLike.getEventId(), likedEvents.iterator().next());
    }

    private EventLike getEventLikeWithUser(EventLikeDto createdDto) {
        EventLike eventLike = repository.findById(createdDto.getId())
                .orElseThrow(() -> new CannotFindEntityException("Cannot find Event Like by id " + createdDto.getId()));
        SystemUser user = userRepository.findAll().iterator().next();
        HashSet<SystemUser> users = new HashSet<>();
        users.add(user);
        eventLike.setUsers(users);
        return eventLikeRepository.save(eventLike);
    }

    @Test
    public void deleteByEventId() {
        EventLikeDto eventLikeDto = getCreatedEntityId(dto);
        ((EventLikeService) crudService).deleteByEventId(eventLikeDto.getEventId());

        assertEquals(1, eventLikeRepository.findAll().size());
    }

    @Test
    public void deleteByUserId() {
        EventLikeDto eventLikeDto = getCreatedEntityId(dto);
        EventLike eventLike = getEventLikeWithUser(eventLikeDto);

        Optional<EventLike> eventLikeOptional = eventLikeRepository.findById(eventLike.getId());
        assertTrue(eventLikeOptional.isPresent());
        assertEquals(1, eventLikeOptional.get().getUsers().size());

        ((EventLikeService) crudService).deleteByUserId(null, eventLike.getUsers().iterator().next().getId());

        eventLikeOptional = eventLikeRepository.findById(eventLike.getId());
        assertTrue(eventLikeOptional.isPresent());
        assertEquals(0, eventLikeOptional.get().getUsers().size());
    }

    @Override
    public void updateTestWithNotPresentedId() {
    }

    @Override
    protected EventLikeDto getCreatedEntityId(EventLikeDto dto) {
        return dto;
    }

    @Override
    protected EventLikeDto createDto() {
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
        prepareDto(userDto);
        IdDto idDto = userService.create(userDto);
        Optional<EventLike> eventLike = eventLikeRepository.findByEventId(idDto.getId());
        assertTrue(eventLike.isPresent());
        return eventLikeMapper.entityToDto(eventLike.get());
    }

    private void prepareDto(UserDto userDto) {
        when(locationClient.validate("groupLocations", userDto.getLocation())).thenReturn(new ValidationMessageDto(EMPTY));
        when(contentClient.validate("groupPhotos", userDto.getGroupPhoto())).thenReturn(new ValidationMessageDto(EMPTY));
        when(contentClient.update("groupPhotos", userDto.getGroupPhoto())).thenReturn(new IdDto(userDto.getGroupPhoto().getId()));
        when(contentClient.create("groupPhotos", userDto.getGroupPhoto())).thenReturn(new IdDto(userDto.getGroupPhoto().getId()));
        when(locationClient.readGroupLocations(userDto.getLocation().getId())).thenReturn(userDto.getLocation());
        when(locationClient.create("groupLocations", userDto.getLocation())).thenReturn(new IdDto(userDto.getLocation().getId()));
        when(locationClient.update("groupLocations", userDto.getLocation())).thenReturn(new IdDto(userDto.getLocation().getId()));
        when(contentClient.readGroupPhotos(userDto.getGroupPhoto().getId())).thenReturn(userDto.getGroupPhoto());

    }
}