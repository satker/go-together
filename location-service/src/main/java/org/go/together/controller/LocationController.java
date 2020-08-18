package org.go.together.controller;

import org.go.together.client.LocationClient;
import org.go.together.dto.*;
import org.go.together.find.controller.FindController;
import org.go.together.find.dto.form.FormDto;
import org.go.together.find.dto.ResponseDto;
import org.go.together.service.GroupLocationService;
import org.go.together.service.PlaceService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@RestController
public class LocationController extends FindController implements LocationClient {
    private final PlaceService placeService;
    private final GroupLocationService groupLocationService;

    public LocationController(PlaceService placeService,
                              GroupLocationService groupLocationService) {
        this.placeService = placeService;
        this.groupLocationService = groupLocationService;
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
