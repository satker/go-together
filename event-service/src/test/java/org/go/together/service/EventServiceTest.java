package org.go.together.service;

import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.client.UserClient;
import org.go.together.context.RepositoryContext;
import org.go.together.dto.EventDto;
import org.go.together.dto.EventPaidThingDto;
import org.go.together.dto.IdDto;
import org.go.together.dto.SimpleDto;
import org.go.together.enums.CrudOperation;
import org.go.together.mapper.PaidThingMapper;
import org.go.together.model.Event;
import org.go.together.model.PaidThing;
import org.go.together.repository.interfaces.PaidThingRepository;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = RepositoryContext.class)
public class EventServiceTest extends CrudServiceCommonTest<Event, EventDto> {
    @Autowired
    private UserClient userClient;

    @Autowired
    private LocationClient locationClient;

    @Autowired
    private ContentClient contentClient;

    @Autowired
    private PaidThingMapper paidThingMapper;

    @Autowired
    private PaidThingRepository paidThingRepository;

    @BeforeEach
    public void init() {
        super.init();
        prepareDto(dto);
        prepareDto(updatedDto);
    }

    @Test
    public void autocompleteEventTest() {
        IdDto idDto = crudService.create(dto);
        Set<SimpleDto> events = ((EventService) crudService).autocompleteEvents(dto.getName());

        assertEquals(1, events.size());
        assertEquals(idDto.getId(), UUID.fromString(events.iterator().next().getId()));
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
        for (EventPaidThingDto paidThingDto : eventDto.getPaidThings()) {
            PaidThing paidThing = paidThingMapper.dtoToEntity(paidThingDto.getPaidThing());
            PaidThing savedPaidThing = paidThingRepository.save(paidThing);
            paidThingDto.setPaidThing(paidThingMapper.entityToDto(savedPaidThing));
        }

        return eventDto;
    }

    private void prepareDto(EventDto eventDto) {
        when(userClient.checkIfUserPresentsById(eventDto.getAuthor().getId())).thenReturn(true);
        when(userClient.findById(eventDto.getAuthor().getId())).thenReturn(eventDto.getAuthor());
        when(locationClient.getRouteById(eventDto.getRoute().getId())).thenReturn(eventDto.getRoute());
        when(contentClient.readGroupPhotosById(eventDto.getGroupPhoto().getId())).thenReturn(eventDto.getGroupPhoto());
        when(locationClient.createRoute(eventDto.getRoute())).thenReturn(new IdDto(eventDto.getRoute().getId()));
        when(locationClient.updateRoute(eventDto.getRoute())).thenReturn(new IdDto(eventDto.getRoute().getId()));
        when(contentClient.updateGroup(eventDto.getGroupPhoto())).thenReturn(new IdDto(eventDto.getGroupPhoto().getId()));
        when(contentClient.createGroup(eventDto.getGroupPhoto())).thenReturn(new IdDto(eventDto.getGroupPhoto().getId()));
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
        assertEquals(dto.getHousingType(), savedObject.getHousingType());
    }
}
