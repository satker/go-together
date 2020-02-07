package org.go.together.controller;

import org.go.together.client.EventClient;
import org.go.together.dto.EventDto;
import org.go.together.dto.IdDto;
import org.go.together.service.EventService;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class EventController implements EventClient {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public EventDto getEventById(UUID eventId) {
        return eventService.read(eventId);
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
}
