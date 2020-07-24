package org.go.together.controller;

import org.go.together.client.LocationClient;
import org.go.together.dto.*;
import org.go.together.dto.filter.FormDto;
import org.go.together.logic.controllers.FindController;
import org.go.together.service.GroupLocationService;
import org.go.together.service.LocationService;
import org.go.together.service.PlaceService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@RestController
public class LocationController extends FindController implements LocationClient {
    private final LocationService locationService;
    private final PlaceService placeService;
    private final GroupLocationService groupLocationService;

    public LocationController(LocationService locationService,
                              PlaceService placeService,
                              GroupLocationService groupLocationService) {
        super(Set.of(locationService, placeService));
        this.locationService = locationService;
        this.placeService = placeService;
        this.groupLocationService = groupLocationService;
    }

    @Override
    public Set<LocationDto> getEventRoute(UUID eventId) {
        return locationService.getEventRoute(eventId);
    }

    @Override
    public GroupLocationDto getRouteById(UUID routeId) {
        return groupLocationService.read(routeId);
    }

    @Override
    public IdDto createRoute(GroupLocationDto groupLocationDto) {
        return groupLocationService.create(groupLocationDto);
    }

    @Override
    public IdDto updateRoute(GroupLocationDto groupLocationDto) {
        return groupLocationService.update(groupLocationDto);
    }

    @Override
    public void deleteRoute(UUID eventRouteId) {
        groupLocationService.delete(eventRouteId);
    }

    @Override
    public String validateRoute(GroupLocationDto groupLocationDto) {
        return groupLocationService.validate(groupLocationDto);
    }

    @Override
    public String validateLocation(PlaceDto placeDto) {
        return placeService.validate(placeDto);
    }

    @Override
    public PlaceDto getLocationById(UUID locationId) {
        return placeService.read(locationId);
    }

    @Override
    public Set<SimpleDto> autocompleteLocations(String name) {
        return placeService.getLocationsByName(name);
    }

    @Override
    public IdDto saveLocation(PlaceDto placeDto) {
        return placeService.create(placeDto);
    }

    @Override
    public ResponseDto<Object> find(FormDto formDto) {
        return super.find(formDto);
    }
}
