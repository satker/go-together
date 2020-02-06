package org.go.together.client;

import org.go.together.dto.EventLocationDto;
import org.go.together.dto.IdDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@FeignClient(name = "location-service", url = "http://localhost:8090")
public interface LocationClient {
    @GetMapping("/events/{eventId}/routes")
    Set<EventLocationDto> getEventRoute(@PathVariable("eventId") UUID eventId);

    @GetMapping("/routes/{routeId}")
    EventLocationDto getRouteById(@PathVariable("routeId") UUID routeId);

    @PostMapping("/routes")
    Set<IdDto> saveOrUpdateEventRoutes(@RequestBody Set<EventLocationDto> eventLocationDtos);

    @DeleteMapping("/routes")
    boolean deleteRoutes(@RequestBody Set<EventLocationDto> eventLocationDtos);

}
