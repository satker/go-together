package org.go.together.client;

import org.go.together.base.FindClient;
import org.go.together.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@FeignClient(name = "event-service")
public interface EventClient extends FindClient {
    @GetMapping("/events/{eventId}")
    EventDto getEventById(@PathVariable("eventId") UUID eventId);

    @PutMapping("/events")
    IdDto createEvent(@RequestBody EventDto eventDto);

    @PostMapping("/events")
    IdDto updateEvent(@RequestBody EventDto eventDto);

    @DeleteMapping("/events/{eventId}")
    IdDto deleteEvent(@PathVariable("eventId") UUID eventId);

    @GetMapping("/events")
    Set<SimpleDto> autocompleteEvents(@RequestParam("name") String name);

    @GetMapping("/events/{eventId}/users/statuses")
    EventUserStatus[] getUserStatuses();

    @PutMapping("/events/users")
    IdDto createEventUser(@RequestBody EventUserDto eventUserDto);

    @PostMapping("/events/users")
    IdDto updateEventUser(@RequestBody EventUserDto eventUserDto);

    @DeleteMapping("/events/users")
    boolean deleteEventUserByEventId(@RequestBody EventUserDto eventUserDto);
}
