package org.go.together.controller;

import lombok.RequiredArgsConstructor;
import org.go.together.base.impl.FindController;
import org.go.together.client.EventClient;
import org.go.together.dto.*;
import org.go.together.service.interfaces.EventService;
import org.go.together.service.interfaces.EventUserService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EventController extends FindController implements EventClient {
    private final EventService eventService;
    private final EventUserService eventUserService;

    @Override
    public EventDto getEventById(UUID eventId) {
        return eventService.read(eventId);
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
    public EventUserStatus[] getUserStatuses() {
        return EventUserStatus.values();
    }

    @Override
    public boolean deleteEventUserByEventId(EventUserDto eventUserDto) {
        return eventUserService.deleteEventUserByEventId(eventUserDto);
    }
}
