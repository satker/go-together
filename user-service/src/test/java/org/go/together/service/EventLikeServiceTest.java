package org.go.together.service;

import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.context.RepositoryContext;
import org.go.together.dto.*;
import org.go.together.mapper.EventLikeMapper;
import org.go.together.mapper.InterestMapper;
import org.go.together.mapper.LanguageMapper;
import org.go.together.model.EventLike;
import org.go.together.repository.EventLikeRepository;
import org.go.together.repository.InterestRepository;
import org.go.together.repository.LanguageRepository;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private InterestMapper interestMapper;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private LanguageMapper languageMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private EventLikeMapper eventLikeMapper;

    @Autowired
    private LocationClient locationClient;


    @BeforeEach
    public void init() {
        super.init();
        updatedDto.setEventId(dto.getEventId());
    }

    @Override
    public void createTest() {
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
        when(contentClient.updateGroup(userDto.getGroupPhoto())).thenReturn(new IdDto(userDto.getGroupPhoto().getId()));
        when(contentClient.createGroup(userDto.getGroupPhoto())).thenReturn(new IdDto(userDto.getGroupPhoto().getId()));
        when(locationClient.saveLocation(userDto.getLocation())).thenReturn(new IdDto(userDto.getLocation().getId()));
        when(locationClient.getLocationById(userDto.getLocation().getId())).thenReturn(userDto.getLocation());
        when(contentClient.readGroupPhotosById(userDto.getGroupPhoto().getId())).thenReturn(userDto.getGroupPhoto());

    }
}