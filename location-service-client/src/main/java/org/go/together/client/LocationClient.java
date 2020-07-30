package org.go.together.client;

import org.go.together.dto.*;
import org.go.together.interfaces.FindClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@FeignClient(name = "location-service")
public interface LocationClient extends FindClient {
    @GetMapping("/events/{eventId}/routes")
    Set<LocationDto> getEventRoute(@PathVariable("eventId") UUID eventId);

    @GetMapping("/routes/{groupLocationId}")
    GroupLocationDto getRouteById(@PathVariable("groupLocationId") UUID routeId);

    @PutMapping("/routes")
    IdDto createRoute(@RequestBody GroupLocationDto groupLocationDto);

    @PostMapping("/routes")
    IdDto updateRoute(@RequestBody GroupLocationDto groupLocationDto);

    @DeleteMapping("/routes/{groupLocationId}")
    void deleteRoute(@PathVariable("groupLocationId") UUID groupLocationId);

    @PostMapping("/routes/validate")
    String validateRoute(@RequestBody GroupLocationDto groupLocationDto);

    @PostMapping("/locations/validate")
    String validateLocation(@RequestBody PlaceDto placeDto);

    @GetMapping("/locations/{locationId}")
    PlaceDto getLocationById(@PathVariable("locationId") UUID locationId);

    @GetMapping("/locations")
    Set<SimpleDto> autocompleteLocations(@RequestParam("name") String name);

    @PostMapping("/locations")
    IdDto saveLocation(@RequestBody PlaceDto placeDto);
}
