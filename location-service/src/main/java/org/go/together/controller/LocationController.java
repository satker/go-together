package org.go.together.controller;

import lombok.RequiredArgsConstructor;
import org.go.together.base.impl.FindController;
import org.go.together.client.LocationClient;
import org.go.together.dto.*;
import org.go.together.dto.form.FormDto;
import org.go.together.service.interfaces.GroupLocationService;
import org.go.together.service.interfaces.PlaceService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class LocationController extends FindController implements LocationClient {
    private final PlaceService placeService;
    private final GroupLocationService groupLocationService;

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
