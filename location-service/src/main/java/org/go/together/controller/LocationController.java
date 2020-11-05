package org.go.together.controller;

import lombok.RequiredArgsConstructor;
import org.go.together.base.FindController;
import org.go.together.client.LocationClient;
import org.go.together.dto.SimpleDto;
import org.go.together.service.interfaces.PlaceService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class LocationController extends FindController implements LocationClient {
    private final PlaceService placeService;

    @Override
    public Set<SimpleDto> autocompleteLocations(String name) {
        return placeService.getLocationsByName(name);
    }
}
