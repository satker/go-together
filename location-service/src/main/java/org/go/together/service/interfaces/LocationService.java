package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.dto.LocationDto;
import org.go.together.find.FindService;
import org.go.together.model.Location;

import java.util.Set;

public interface LocationService extends CrudService<LocationDto>, FindService<Location> {
    Set<Location> saveOrUpdateEventRoutes(Set<LocationDto> locationDtos, Set<Location> presentedLocations);
}
