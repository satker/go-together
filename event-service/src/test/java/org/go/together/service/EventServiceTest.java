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
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ContextConfiguration;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = RepositoryContext.class)
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9097", "port=9097"})
public class EventServiceTest extends CrudServiceCommonTest<Event, EventDto> {
    @Autowired
    private FindProducer<UserDto> findUserKafkaProducer;

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
        when(findUserKafkaProducer.find(any())).thenReturn(objectResponseDto);
        when(userCrudClient.read(eq(eventDto.getAuthor().getId()))).thenReturn(eventDto.getAuthor());
        when(groupPhotoProducer.read(eq(eventDto.getGroupPhoto().getId()))).thenReturn(eventDto.getGroupPhoto());
        when(groupPhotoProducer.update(eq(eventDto.getGroupPhoto()))).thenReturn(new IdDto(eventDto.getGroupPhoto().getId()));
        when(groupPhotoProducer.create(eq(eventDto.getGroupPhoto()))).thenReturn(new IdDto(eventDto.getGroupPhoto().getId()));
        when(groupPhotoValidate.validate(eq(eventDto.getGroupPhoto()))).thenReturn(new ValidationMessageDto(EMPTY));
        when(routeInfoValidator.validate(eq(eventDto.getRouteInfo()))).thenReturn(new ValidationMessageDto(EMPTY));
        when(routeInfoProducer.create(eq(eventDto.getRouteInfo()))).thenReturn(new IdDto(eventDto.getRouteInfo().getId()));
        when(routeInfoProducer.read(eq(eventDto.getRouteInfo().getId()))).thenReturn(eventDto.getRouteInfo());
        when(routeInfoProducer.update(eq(eventDto.getRouteInfo()))).thenReturn(new IdDto(eventDto.getRouteInfo().getId()));
    }

    @Override
    protected void checkDtos(EventDto dto, EventDto savedObject, CrudOperation operation) {
        assertEquals(dto.getAuthor(), savedObject.getAuthor());
        assertEquals(dto.getDescription(), savedObject.getDescription());
        assertEquals(dto.getName(), savedObject.getName());
        assertEquals(dto.getEndDate(), savedObject.getEndDate());
        assertEquals(dto.getStartDate(), savedObject.getStartDate());
        assertEquals(dto.getGroupPhoto(), savedObject.getGroupPhoto());
        assertEquals(dto.getPeopleCount(), savedObject.getPeopleCount());
        assertEquals(dto.getRouteInfo(), savedObject.getRouteInfo());
        assertEquals(dto.getGroupPhoto(), savedObject.getGroupPhoto());
    }
}
