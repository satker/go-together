package org.go.together.controller;

import org.go.together.client.LocationClient;
import org.go.together.dto.EventLocationDto;
import org.go.together.dto.IdDto;
import org.go.together.dto.LocationDto;
import org.go.together.service.EventLocationService;
import org.go.together.service.LocationService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@RestController
public class LocationController implements LocationClient {
    private final EventLocationService eventLocationService;
    private final LocationService locationService;

    public LocationController(EventLocationService eventLocationService,
                              LocationService locationService) {
        this.eventLocationService = eventLocationService;
        this.locationService = locationService;
    }

    @Override
    public Set<EventLocationDto> getEventRoute(UUID eventId) {
        return eventLocationService.getEventRoute(eventId);
    }

    @Override
    public EventLocationDto getRouteById(UUID routeId) {
        return eventLocationService.read(routeId);
    }

    @Override
    public Set<IdDto> saveOrUpdateEventRoutes(Set<EventLocationDto> eventLocationDtos) {
        return eventLocationService.saveOrUpdateEventRoutes(eventLocationDtos);
    }

    @Override
    public boolean deleteRoutes(Set<UUID> eventLocationDtos) {
        return eventLocationService.deleteByEventId(eventLocationDtos);
    }

    @Override
    public String validate(EventLocationDto eventLocationDto) {
        return eventLocationService.validate(eventLocationDto);
    }

    @Override
    public LocationDto getLocationById(UUID locationId) {
        return locationService.read(locationId);
    }
}
