package org.go.together.service;

import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.client.RouteInfoClient;
import org.go.together.client.UserClient;
import org.go.together.context.RepositoryContext;
import org.go.together.dto.EventDto;
import org.go.together.dto.GroupPhotoDto;
import org.go.together.dto.IdDto;
import org.go.together.dto.ValidationMessageDto;
import org.go.together.enums.CrudOperation;
import org.go.together.kafka.interfaces.producers.crud.ReadKafkaProducer;
import org.go.together.model.Event;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ContextConfiguration;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = RepositoryContext.class)
@EmbeddedKafka
public class EventServiceTest extends CrudServiceCommonTest<Event, EventDto> {
    @Autowired
    private UserClient userClient;

    @Autowired
    private LocationClient locationClient;

    @Autowired
    private ReadKafkaProducer<GroupPhotoDto> groupPhotoProducer;

    @Autowired
    private ContentClient contentClient;

    @Autowired
    private RouteInfoClient routeInfoClient;

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
        when(userClient.checkIfUserPresentsById(eventDto.getAuthor().getId())).thenReturn(true);
        when(userClient.readUser(eventDto.getAuthor().getId())).thenReturn(eventDto.getAuthor());
        when(locationClient.readGroupLocations(eventDto.getRoute().getId())).thenReturn(eventDto.getRoute());
        when(groupPhotoProducer.read(any(UUID.class), eq(eventDto.getGroupPhoto().getId()))).thenReturn(eventDto.getGroupPhoto());
        when(locationClient.create("groupLocations", eventDto.getRoute())).thenReturn(new IdDto(eventDto.getRoute().getId()));
        when(locationClient.update("groupLocations", eventDto.getRoute())).thenReturn(new IdDto(eventDto.getRoute().getId()));
        when(contentClient.update("groupPhotos", eventDto.getGroupPhoto())).thenReturn(new IdDto(eventDto.getGroupPhoto().getId()));
        when(contentClient.create("groupPhotos", eventDto.getGroupPhoto())).thenReturn(new IdDto(eventDto.getGroupPhoto().getId()));
        when(contentClient.validate("groupPhotos", eventDto.getGroupPhoto())).thenReturn(new ValidationMessageDto(EMPTY));
        when(locationClient.validate("groupLocations", eventDto.getRoute())).thenReturn(new ValidationMessageDto(EMPTY));
        when(routeInfoClient.validate("groupRouteInfo", eventDto.getRouteInfo())).thenReturn(new ValidationMessageDto(EMPTY));
        when(routeInfoClient.create("groupRouteInfo", eventDto.getRouteInfo())).thenReturn(new IdDto(eventDto.getRouteInfo().getId()));
        when(routeInfoClient.update("groupRouteInfo", eventDto.getRouteInfo())).thenReturn(new IdDto(eventDto.getRouteInfo().getId()));
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
