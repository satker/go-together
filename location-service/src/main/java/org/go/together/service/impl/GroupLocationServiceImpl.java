package org.go.together.service.impl;

import lombok.RequiredArgsConstructor;
import org.go.together.base.CommonCrudService;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.GroupLocationDto;
import org.go.together.dto.LocationDto;
import org.go.together.enums.CrudOperation;
import org.go.together.model.GroupLocation;
import org.go.together.model.Location;
import org.go.together.service.interfaces.GroupLocationService;
import org.go.together.service.interfaces.LocationService;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.go.together.enums.LocationServiceInfo.GROUP_LOCATION;

@Service
@RequiredArgsConstructor
public class GroupLocationServiceImpl extends CommonCrudService<GroupLocationDto, GroupLocation>
        implements GroupLocationService {
    private final LocationService locationService;

    @Override
    protected GroupLocation enrichEntity(UUID requestId, GroupLocation entity, GroupLocationDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.CREATE) {
            Set<LocationDto> locationDtos = dto.getLocations();
            Set<Location> savedLocations = locationService.saveOrUpdateEventRoutes(requestId, locationDtos, Collections.emptySet());
            entity.setLocations(savedLocations);
        } else if (crudOperation == CrudOperation.UPDATE) {
            Set<LocationDto> locationDtos = dto.getLocations();
            GroupLocation groupLocation = repository.findByIdOrThrow(entity.getId());
            Set<Location> savedLocations = locationService.saveOrUpdateEventRoutes(requestId, locationDtos, groupLocation.getLocations());
            entity.setLocations(savedLocations);
        } else if (crudOperation == CrudOperation.DELETE) {
            GroupLocation groupLocation = repository.findByIdOrThrow(entity.getId());
            Optional.ofNullable(groupLocation.getLocations())
                    .orElse(Collections.emptySet())
                    .stream()
                    .map(Location::getId)
                    .forEach(locationId -> locationService.delete(requestId, locationId));
            groupLocation.setLocations(Collections.emptySet());
        }
        return entity;
    }

    @Override
    public String getServiceName() {
        return GROUP_LOCATION.getDescription();
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return Map.of("locations", FieldMapper.builder()
                .innerService(locationService)
                .currentServiceField("locations").build());
    }
}
