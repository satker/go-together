package org.go.together.notification.client;

import org.go.together.dto.IdDto;
import org.go.together.dto.SimpleDto;
import org.go.together.find.client.FindClient;
import org.go.together.notification.dto.EventDto;
import org.go.together.notification.dto.EventUserDto;
import org.go.together.notification.dto.EventUserStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
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
    void deleteEvent(@PathVariable("eventId") UUID eventId);

    @GetMapping("/events")
    Set<SimpleDto> autocompleteEvents(@RequestParam("name") String name);

    @GetMapping("/events/housingTypes")
    Collection<SimpleDto> getHousingTypes();

    @GetMapping("/events/payedThings")
    Collection<Object> getPaidThings();

    @GetMapping("/events/cashCategories")
    Collection<SimpleDto> getCashCategories();

    @GetMapping("/events/{eventId}/users/statuses")
    EventUserStatus[] getUserStatuses();

    @GetMapping("/events/{eventId}/users")
    Collection<EventUserDto> getEventUsersByEventId(@PathVariable("eventId") UUID eventId);

    @PutMapping("/events/users")
    IdDto createEventUser(@RequestBody EventUserDto eventUserDto);

    @PostMapping("/events/users")
    IdDto updateEventUser(@RequestBody EventUserDto eventUserDto);

    @DeleteMapping("/events/users")
    boolean deleteEventUserByEventId(@RequestBody EventUserDto eventUserDto);
}