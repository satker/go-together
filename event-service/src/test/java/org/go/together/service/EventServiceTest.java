package org.go.together.service;

import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.client.UserClient;
import org.go.together.context.RepositoryContext;
import org.go.together.dto.*;
import org.go.together.enums.CrudOperation;
import org.go.together.mapper.PaidThingMapper;
import org.go.together.model.Event;
import org.go.together.model.PaidThing;
import org.go.together.repository.PaidThingRepository;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

        Collection<EventLocationDto> routes = eventDto.getRoute();
        Set<Integer> numbers = IntStream.rangeClosed(1, routes.size())
                .boxed()
                .collect(Collectors.toSet());
        Iterator<EventLocationDto> iterator = routes.iterator();
        for (Integer number : numbers) {
            EventLocationDto eventLocationDto = iterator.next();
            eventLocationDto.setRouteNumber(number);
            if (number == 1) {
                eventLocationDto.setIsStart(true);
                eventLocationDto.setIsEnd(false);
            } else if (number == routes.size()) {
                eventLocationDto.setIsStart(false);
                eventLocationDto.setIsEnd(true);
            } else {
                eventLocationDto.setIsStart(false);
                eventLocationDto.setIsEnd(false);
            }
        }

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
        for (EventLocationDto eventLocationDto : eventDto.getRoute()) {
            when(locationClient.getRouteById(eventLocationDto.getId())).thenReturn(eventLocationDto);
        }
        when(contentClient.readGroupPhotosById(eventDto.getGroupPhoto().getId())).thenReturn(eventDto.getGroupPhoto());
        when(locationClient.saveOrUpdateEventRoutes(eq(eventDto.getRoute()), any()))
                .thenReturn(eventDto.getRoute().stream()
                        .map(EventLocationDto::getId)
                        .map(IdDto::new)
                        .collect(Collectors.toSet())
                );
        when(contentClient.updateGroup(eventDto.getGroupPhoto())).thenReturn(new IdDto(eventDto.getGroupPhoto().getId()));
        when(contentClient.createGroup(eventDto.getGroupPhoto())).thenReturn(new IdDto(eventDto.getGroupPhoto().getId()));
    }

    @Override
    protected void checkDtos(EventDto dto, EventDto savedObject, CrudOperation operation) {
        assertEquals(dto.getAuthor(), savedObject.getAuthor());
        assertTrue(dto.getRoute().stream().allMatch(route -> savedObject.getRoute().stream().anyMatch(route::equals)));
        assertEquals(dto.getDescription(), savedObject.getDescription());
        assertEquals(dto.getName(), savedObject.getName());
        assertEquals(dto.getEndDate(), savedObject.getEndDate());
        assertEquals(dto.getStartDate(), savedObject.getStartDate());
        assertEquals(dto.getGroupPhoto(), savedObject.getGroupPhoto());
        assertEquals(dto.getPeopleCount(), savedObject.getPeopleCount());
        assertEquals(dto.getHousingType(), savedObject.getHousingType());
    }
}
