package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.base.FindService;
import org.go.together.dto.LocationDto;
import org.go.together.model.Location;

import java.util.Set;
import java.util.UUID;

public interface LocationService extends CrudService<LocationDto>, FindService<LocationDto> {
    Set<Location> saveOrUpdateEventRoutes(UUID requestId, Set<LocationDto> locationDtos, Set<Location> presentedLocations);
}
