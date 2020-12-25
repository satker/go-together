package org.go.together.client;

import org.go.together.base.FindClient;
import org.go.together.dto.EventDto;
import org.go.together.dto.EventUserDto;
import org.go.together.dto.EventUserStatus;
import org.go.together.dto.IdDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "event-service")
public interface EventClient extends FindClient {
    @GetMapping("/events/{id}")
    EventDto readEvent(@PathVariable("id") UUID id);

    @GetMapping("/eventUsers/statuses")
    EventUserStatus[] getUserStatuses();

    @PutMapping("/eventUsers")
    IdDto createEventUsers(@RequestBody EventUserDto dto);

    @PostMapping("/eventUsers")
    IdDto updateEventUsers(@RequestBody EventUserDto dto);

    @PutMapping("/events")
    IdDto createEvent(@RequestBody EventDto dto);

    @PostMapping("/events")
    IdDto updateEvent(@RequestBody EventDto dto);

    @DeleteMapping("/events/{id}")
    void delete(@PathVariable("id") UUID dtoId);

    @DeleteMapping("/eventsUsers")
    boolean deleteEventUserByEventId(@RequestBody EventUserDto eventUserDto);
}
