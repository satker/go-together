package org.go.together.client;

import org.go.together.dto.*;
import org.go.together.dto.filter.FormDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@FeignClient(name = "location-service")
public interface LocationClient {
    @GetMapping("/events/{eventId}/routes")
    Set<EventLocationDto> getEventRoute(@PathVariable("eventId") UUID eventId);

    @GetMapping("/routes/{routeId}")
    EventLocationDto getRouteById(@PathVariable("routeId") UUID routeId);

    @PostMapping("/routes")
    Set<IdDto> saveOrUpdateEventRoutes(@RequestBody Set<EventLocationDto> eventLocationDtos);

    @DeleteMapping("/routes")
    boolean deleteRoutes(@RequestBody Set<UUID> eventLocationDtos);

    @PostMapping("/routes/validate")
    String validateRoutes(@RequestBody EventLocationDto eventLocationDto);

    @PostMapping("/locations/validate")
    String validateLocation(@RequestBody LocationDto locationDto);

    @GetMapping("/locations/{locationId}")
    LocationDto getLocationById(@PathVariable("locationId") UUID locationId);

    @GetMapping("/locations")
    Set<SimpleDto> autocompleteLocations(@RequestParam("name") String name);

    @PostMapping("/locations")
    IdDto saveLocation(@RequestBody LocationDto locationDto);

    @PostMapping("/find")
    ResponseDto<Object> find(@RequestBody FormDto formDto);
}
