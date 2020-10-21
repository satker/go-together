package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.dto.PlaceDto;
import org.go.together.dto.SimpleDto;
import org.go.together.find.FindService;
import org.go.together.model.Place;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PlaceService extends CrudService<PlaceDto>, FindService<Place> {
    Optional<Place> getPlaceEquals(PlaceDto anotherPlaceDto);

    Optional<Place> getPlaceByLocationId(UUID locationId);

    Set<SimpleDto> getLocationsByName(String location);
}
