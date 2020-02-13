package org.go.together.controller;

import org.go.together.client.EventClient;
import org.go.together.dto.*;
import org.go.together.dto.filter.FormDto;
import org.go.together.service.EventService;
import org.go.together.service.PaidThingService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class EventController implements EventClient {
    private final EventService eventService;
    private final PaidThingService paidThingService;

    public EventController(EventService eventService,
                           PaidThingService paidThingService) {
        this.eventService = eventService;
        this.paidThingService = paidThingService;
    }

    @Override
    public EventDto getEventById(UUID eventId) {
        return eventService.read(eventId);
    }

    @Override
    public ResponseDto<EventDto> find(FormDto formDto) {
        return new ResponseDto<EventDto>(formDto.getPage(), eventService.findAll());
    }

    @Override
    public IdDto createEvent(EventDto eventDto) {
        return eventService.create(eventDto);
    }

    @Override
    public IdDto updateEvent(EventDto eventDto) {
        return eventService.update(eventDto);
    }

    @Override
    public void deleteEvent(UUID eventId) {
        eventService.delete(eventId);
    }

    @Override
    public Set<SimpleDto> autocompleteEvents(String name) {
        return eventService.autocompleteEvents(name);
    }

    @Override
    public Collection<SimpleDto> getHousingTypes() {
        return Stream.of(HousingType.values())
                .map(type -> new SimpleDto(type.name(), type.getDescription()))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<PaidThingDto> getPaidThings() {
        return paidThingService.getPaidThings();
    }

    @Override
    public Collection<SimpleDto> getCashCategories() {
        return Stream.of(CashCategory.values())
                .map(type -> new SimpleDto(type.name(), type.getDescription()))
                .collect(Collectors.toSet());
    }
}
