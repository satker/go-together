package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.*;
import org.go.together.enums.CrudOperation;
import org.go.together.kafka.producers.CrudProducer;
import org.go.together.kafka.producers.FindProducer;
import org.go.together.model.Event;
import org.go.together.model.EventUser;
import org.go.together.repository.interfaces.EventRepository;
import org.go.together.service.interfaces.EventUserService;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = RepositoryContext.class)
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9098", "port=9098"})
public class EventUserServiceTest extends CrudServiceCommonTest<EventUser, EventUserDto> {
    @Autowired
    private CrudProducer<UserDto> usersCrudProducer;

    @Autowired
    private FindProducer<UserDto> findUserKafkaProducer;

    @Autowired
    private EventRepository eventRepository;


    @Override
    @BeforeEach
    public void init() {
        super.init();
        ResponseDto<Object> objectResponseDto = new ResponseDto<>();
        objectResponseDto.setPage(null);
        objectResponseDto.setResult(Collections.singleton(dto.getUser().getId()));
        when(findUserKafkaProducer.find(any())).thenReturn(objectResponseDto);

        ResponseDto<Object> updatedObjectResponseDto = new ResponseDto<>();
        updatedObjectResponseDto.setPage(null);
        updatedObjectResponseDto.setResult(Collections.singleton(updatedDto.getUser().getId()));
        when(findUserKafkaProducer.find(any())).thenReturn(updatedObjectResponseDto);

        when(usersCrudProducer.read(eq(dto.getUser().getId())))
                .thenReturn(simpleUserDtoToUserDto(dto.getUser()));
        when(usersCrudProducer.read(eq(updatedDto.getUser().getId())))
                .thenReturn(simpleUserDtoToUserDto(updatedDto.getUser()));
        Event entity = new Event();
        entity.setId(dto.getEventId());
        eventRepository.save(entity);
        entity.setId(updatedDto.getEventId());
        eventRepository.save(entity);
    }

    private UserDto simpleUserDtoToUserDto(SimpleUserDto simpleUserDto) {
        GroupPhotoDto dto = new GroupPhotoDto();
        dto.setPhotos(Collections.singleton(simpleUserDto.getUserPhoto()));
        UserDto userDto = new UserDto();
        userDto.setId(simpleUserDto.getId());
        userDto.setFirstName(simpleUserDto.getFirstName());
        userDto.setLastName(simpleUserDto.getLastName());
        userDto.setLogin(simpleUserDto.getLogin());
        userDto.setGroupPhoto(dto);
        return userDto;
    }

    @Test
    public void deleteEventUserByEventIdTest() {
        crudService.create(dto);
        boolean isDeleted = ((EventUserService) crudService).deleteEventUserByEventId(dto);

        assertTrue(isDeleted);
    }

    @Override
    protected EventUserDto createDto() {
        return factory.manufacturePojo(EventUserDto.class);
    }

    @Override
    protected void checkDtos(EventUserDto dto, EventUserDto savedObject, CrudOperation operation) {
        assertEquals(dto, savedObject);
    }
}
