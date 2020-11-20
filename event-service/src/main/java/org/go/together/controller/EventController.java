package org.go.together.controller;

import lombok.RequiredArgsConstructor;
import org.go.together.base.FindController;
import org.go.together.client.EventClient;
import org.go.together.dto.EventDto;
import org.go.together.dto.EventUserDto;
import org.go.together.dto.EventUserStatus;
import org.go.together.service.interfaces.EventService;
import org.go.together.service.interfaces.EventUserService;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EventController extends FindController implements EventClient {
    private final EventService eventService;
    private final EventUserService eventUserService;

    @Override
    public EventDto readEvent(UUID id) {
        return eventService.read(id);
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
