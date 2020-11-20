package org.go.together.controller;

import lombok.RequiredArgsConstructor;
import org.go.together.base.FindController;
import org.go.together.client.LocationClient;
import org.go.together.dto.GroupLocationDto;
import org.go.together.dto.SimpleDto;
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
    public Set<SimpleDto> autocompleteLocations(String name) {
        return placeService.getLocationsByName(name);
    }

    @Override
    public GroupLocationDto readGroupLocations(UUID routeId) {
        return groupLocationService.read(routeId);
    }
}
