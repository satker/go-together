package org.go.together.client;

import org.go.together.dto.*;
import org.go.together.dto.filter.FormDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@FeignClient(name = "event-service")
public interface EventClient {
    @GetMapping("/events/{eventId}")
    EventDto getEventById(@PathVariable("eventId") UUID eventId);

    @PostMapping("/find")
    ResponseDto find(@RequestBody FormDto formDto);

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
    Collection<PaidThingDto> getPaidThings();

    @GetMapping("/events/cashCategories")
    Collection<SimpleDto> getCashCategories();

    @GetMapping("/events/{eventId}/users/statuses")
    EventUserStatus[] getUserStatuses();

    @GetMapping("/events/{eventId}/users")
    Collection<EventUserDto> getEventUsersByEventId(@PathVariable("eventId") UUID eventId);

    @PostMapping("/events/users")
    IdDto saveEventUserByEventId(@RequestBody EventUserDto eventUserDto);

    @DeleteMapping("/events/users")
    boolean deleteEventUserByEventId(@RequestBody EventUserDto eventUserDto);
}
