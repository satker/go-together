package org.go.together.client;

import org.go.together.dto.EventDto;
import org.go.together.dto.IdDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "content-service", url = "http://localhost:8081")
public interface EventClient {
    @GetMapping("/events/{eventId}")
    EventDto getEventById(@PathVariable("eventId") UUID eventId);

    @PutMapping("/events")
    IdDto createEvent(@RequestBody EventDto eventDto);

    @PostMapping("/events")
    IdDto updateEvent(@RequestBody EventDto eventDto);

    @DeleteMapping("/events/{eventId}")
    void deleteEvent(@PathVariable("eventId") UUID eventId);
}
