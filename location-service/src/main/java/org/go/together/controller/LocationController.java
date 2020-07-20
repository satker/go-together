package org.go.together.controller;

import org.go.together.client.LocationClient;
import org.go.together.dto.*;
import org.go.together.dto.filter.FormDto;
import org.go.together.logic.controllers.FindController;
import org.go.together.service.EventLocationService;
import org.go.together.service.LocationService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@RestController
public class LocationController extends FindController implements LocationClient {
    private final EventLocationService eventLocationService;
    private final LocationService locationService;

    public LocationController(EventLocationService eventLocationService,
                              LocationService locationService) {
        super(Set.of(eventLocationService, locationService));
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
    public Set<IdDto> saveOrUpdateEventRoutes(Collection<EventLocationDto> eventLocationDtos, UUID eventId) {
        return eventLocationService.saveOrUpdateEventRoutes(eventLocationDtos, eventId);
    }

    @Override
    public void deleteRoute(UUID eventRouteId) {
        eventLocationService.delete(eventRouteId);
    }

    @Override
    public String validateRoutes(EventLocationDto eventLocationDto) {
        return eventLocationService.validate(eventLocationDto);
    }

    @Override
    public String validateLocation(LocationDto locationDto) {
        return locationService.validate(locationDto);
    }

    @Override
    public LocationDto getLocationById(UUID locationId) {
        return locationService.read(locationId);
    }

    @Override
    public Set<SimpleDto> autocompleteLocations(String name) {
        return locationService.getLocationsByName(name);
    }

    @Override
    public IdDto saveLocation(LocationDto locationDto) {
        return locationService.create(locationDto);
    }

    @Override
    public ResponseDto<Object> find(FormDto formDto) {
        return super.find(formDto);
    }
}
