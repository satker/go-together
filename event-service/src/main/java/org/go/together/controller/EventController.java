package org.go.together.controller;

import org.go.together.client.EventClient;
import org.go.together.dto.*;
import org.go.together.find.controller.FindController;
import org.go.together.find.dto.ResponseDto;
import org.go.together.find.dto.form.FormDto;
import org.go.together.service.interfaces.EventService;
import org.go.together.service.interfaces.EventUserService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class EventController extends FindController implements EventClient {
    private final EventService eventService;
    private final EventUserService eventUserService;

    public EventController(EventService eventService,
                           EventUserService eventUserService) {
        this.eventService = eventService;
        this.eventUserService = eventUserService;
    }

    @Override
    public EventDto getEventById(UUID eventId) {
        return eventService.read(eventId);
    }

    @Override
    public ResponseDto<Object> find(FormDto formDto) {
        return super.find(formDto);
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
    public IdDto deleteEvent(UUID eventId) {
        eventService.delete(eventId);
        return new IdDto(eventId);
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
    public Collection<SimpleDto> getCashCategories() {
        return Stream.of(CashCategory.values())
                .map(type -> new SimpleDto(type.name(), type.getDescription()))
                .collect(Collectors.toSet());
    }

    @Override
    public EventUserStatus[] getUserStatuses() {
        return EventUserStatus.values();
    }

    @Override
    public IdDto createEventUser(EventUserDto eventUserDto) {
        return eventUserService.create(eventUserDto);
    }

    @Override
    public IdDto updateEventUser(EventUserDto eventUserDto) {
        return eventUserService.update(eventUserDto);
    }

    @Override
    public boolean deleteEventUserByEventId(EventUserDto eventUserDto) {
        return eventUserService.deleteEventUserByEventId(eventUserDto);
    }
}
