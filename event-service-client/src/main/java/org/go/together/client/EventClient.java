package org.go.together.client;

import org.go.together.dto.EventDto;
import org.go.together.dto.IdDto;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.SimpleDto;
import org.go.together.dto.filter.FormDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@FeignClient(name = "event-service", url = "http://localhost:8081")
public interface EventClient {
    @GetMapping("/events/{eventId}")
    EventDto getEventById(@PathVariable("eventId") UUID eventId);

    @PostMapping("/events/find")
    ResponseDto<EventDto> find(@RequestBody FormDto formDto);

    @PutMapping("/events")
    IdDto createEvent(@RequestBody EventDto eventDto);

    @PostMapping("/events")
    IdDto updateEvent(@RequestBody EventDto eventDto);

    @DeleteMapping("/events/{eventId}")
    void deleteEvent(@PathVariable("eventId") UUID eventId);

    @GetMapping("/events")
    Set<SimpleDto> autocompleteEvents(@RequestParam("name") String name);
}
