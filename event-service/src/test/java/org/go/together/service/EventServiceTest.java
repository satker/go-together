package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.*;
import org.go.together.enums.CrudOperation;
import org.go.together.kafka.producers.CrudProducer;
import org.go.together.kafka.producers.FindProducer;
import org.go.together.kafka.producers.ValidationProducer;
import org.go.together.model.Event;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = RepositoryContext.class)
public class EventServiceTest extends CrudServiceCommonTest<Event, EventDto> {
    @Autowired
    private FindProducer<UserDto> findUserKafkaProducer;

    @Autowired
    private CrudProducer<GroupLocationDto> groupLocationProducer;

    @Autowired
    private ValidationProducer<GroupLocationDto> groupLocationValidate;

    @Autowired
    private CrudProducer<GroupPhotoDto> groupPhotoProducer;

    @Autowired
    private CrudProducer<UserDto> userCrudClient;

    @Autowired
    private ValidationProducer<GroupPhotoDto> groupPhotoValidate;

    @Autowired
    private ValidationProducer<GroupRouteInfoDto> routeInfoValidator;

    @Autowired
    private CrudProducer<GroupRouteInfoDto> routeInfoProducer;

    @Override
    @BeforeEach
    public void init() {
        super.init();
        prepareDto(dto);
        prepareDto(updatedDto);
    }

    @Override
    protected EventDto createDto() {
        EventDto eventDto = factory.manufacturePojo(EventDto.class);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, rand.nextInt(10) + 1);
        eventDto.setStartDate(calendar.getTime());

        calendar.add(Calendar.MONTH, rand.nextInt(10) + 1);
        eventDto.setEndDate(calendar.getTime());
        return eventDto;
    }

    private void prepareDto(EventDto eventDto) {
        ResponseDto<Object> objectResponseDto = new ResponseDto<>();
        objectResponseDto.setPage(null);
        objectResponseDto.setResult(Collections.singleton(eventDto.getAuthor().getId()));
        when(findUserKafkaProducer.find(any(UUID.class), any())).thenReturn(objectResponseDto);
        when(userCrudClient.read(any(UUID.class), eq(eventDto.getAuthor().getId()))).thenReturn(eventDto.getAuthor());
        when(groupLocationProducer.read(any(UUID.class), eq(eventDto.getRoute().getId()))).thenReturn(eventDto.getRoute());
        when(groupPhotoProducer.read(any(UUID.class), eq(eventDto.getGroupPhoto().getId()))).thenReturn(eventDto.getGroupPhoto());
        when(groupLocationProducer.create(any(UUID.class), eq(eventDto.getRoute()))).thenReturn(new IdDto(eventDto.getRoute().getId()));
        when(groupLocationProducer.update(any(UUID.class), eq(eventDto.getRoute()))).thenReturn(new IdDto(eventDto.getRoute().getId()));
        when(groupPhotoProducer.update(any(UUID.class), eq(eventDto.getGroupPhoto()))).thenReturn(new IdDto(eventDto.getGroupPhoto().getId()));
        when(groupPhotoProducer.create(any(UUID.class), eq(eventDto.getGroupPhoto()))).thenReturn(new IdDto(eventDto.getGroupPhoto().getId()));
        when(groupPhotoValidate.validate(any(UUID.class), eq(eventDto.getGroupPhoto()))).thenReturn(new ValidationMessageDto(EMPTY));
        when(groupLocationValidate.validate(any(UUID.class), eq(eventDto.getRoute()))).thenReturn(new ValidationMessageDto(EMPTY));
        when(routeInfoValidator.validate(any(UUID.class), eq(eventDto.getRouteInfo()))).thenReturn(new ValidationMessageDto(EMPTY));
        when(routeInfoProducer.create(any(UUID.class), eq(eventDto.getRouteInfo()))).thenReturn(new IdDto(eventDto.getRouteInfo().getId()));
        when(routeInfoProducer.read(any(UUID.class), eq(eventDto.getRouteInfo().getId()))).thenReturn(eventDto.getRouteInfo());
        when(routeInfoProducer.update(any(UUID.class), eq(eventDto.getRouteInfo()))).thenReturn(new IdDto(eventDto.getRouteInfo().getId()));
    }

    @Override
    protected void checkDtos(EventDto dto, EventDto savedObject, CrudOperation operation) {
        assertEquals(dto.getAuthor(), savedObject.getAuthor());
        assertTrue(dto.getRoute().getLocations().stream()
                .allMatch(route -> savedObject.getRoute().getLocations().stream().anyMatch(route::equals)));
        assertEquals(dto.getDescription(), savedObject.getDescription());
        assertEquals(dto.getName(), savedObject.getName());
        assertEquals(dto.getEndDate(), savedObject.getEndDate());
        assertEquals(dto.getStartDate(), savedObject.getStartDate());
        assertEquals(dto.getGroupPhoto(), savedObject.getGroupPhoto());
        assertEquals(dto.getPeopleCount(), savedObject.getPeopleCount());
    }
}
