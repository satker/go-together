package org.go.together.service;

import org.go.together.base.Mapper;
import org.go.together.context.RepositoryContext;
import org.go.together.dto.*;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.kafka.producers.CrudProducer;
import org.go.together.kafka.producers.ValidationProducer;
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
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = RepositoryContext.class)
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9096", "port=9096"})
class EventLikeServiceTest extends CrudServiceCommonTest<EventLike, EventLikeDto> {
    @Autowired
    private CrudProducer<GroupPhotoDto> groupPhotoCrudClient;

    @Autowired
    private ValidationProducer<GroupPhotoDto> groupPhotoValidate;

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
    private CrudProducer<LocationDto> locationProducer;

    @Autowired
    private ValidationProducer<LocationDto> locationValidate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        super.init();
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

        ((EventLikeService) crudService).deleteByUserId(eventLike.getUsers().iterator().next().getId());

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
                .map(interest -> interestMapper.entityToDto(interest))
                .collect(Collectors.toSet());
        userDto.setInterests(interests);
        Set<LanguageDto> languages = userDto.getLanguages().stream()
                .map(languageMapper::dtoToEntity)
                .peek(languageRepository::save)
                .map(language -> languageMapper.entityToDto(language))
                .collect(Collectors.toSet());
        userDto.setLanguages(languages);
        prepareDto(userDto);
        IdDto idDto = userService.create(userDto);
        Optional<EventLike> eventLike = eventLikeRepository.findByEventId(idDto.getId());
        assertTrue(eventLike.isPresent());
        return eventLikeMapper.entityToDto(eventLike.get());
    }

    private void prepareDto(UserDto userDto) {
        when(locationValidate.validate(eq(userDto.getLocation()))).thenReturn(new ValidationMessageDto(EMPTY));
        when(groupPhotoValidate.validate(eq(userDto.getGroupPhoto()))).thenReturn(new ValidationMessageDto(EMPTY));
        when(groupPhotoCrudClient.update(eq(userDto.getGroupPhoto()))).thenReturn(new IdDto(userDto.getGroupPhoto().getId()));
        when(groupPhotoCrudClient.create(eq(userDto.getGroupPhoto()))).thenReturn(new IdDto(userDto.getGroupPhoto().getId()));
        when(locationProducer.read(eq(userDto.getLocation().getId()))).thenReturn(userDto.getLocation());
        when(locationProducer.create(eq(userDto.getLocation()))).thenReturn(new IdDto(userDto.getLocation().getId()));
        when(locationProducer.update(eq(userDto.getLocation()))).thenReturn(new IdDto(userDto.getLocation().getId()));
        when(groupPhotoCrudClient.read(eq(userDto.getGroupPhoto().getId()))).thenReturn(userDto.getGroupPhoto());

    }
}