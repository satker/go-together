package org.go.together.service.impl;

import lombok.RequiredArgsConstructor;
import org.go.together.base.impl.CommonCrudService;
import org.go.together.dto.FieldMapper;
import org.go.together.dto.GroupLocationDto;
import org.go.together.dto.LocationDto;
import org.go.together.enums.CrudOperation;
import org.go.together.model.GroupLocation;
import org.go.together.model.Location;
import org.go.together.service.interfaces.GroupLocationService;
import org.go.together.service.interfaces.LocationService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GroupLocationServiceImpl extends CommonCrudService<GroupLocationDto, GroupLocation>
        implements GroupLocationService {
    private final LocationService locationService;

    @Override
    protected GroupLocation enrichEntity(GroupLocation entity, GroupLocationDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.CREATE) {
            Set<LocationDto> locationDtos = dto.getLocations();
            Set<Location> savedLocations = locationService.saveOrUpdateEventRoutes(locationDtos, Collections.emptySet());
            entity.setLocations(savedLocations);
        } else if (crudOperation == CrudOperation.UPDATE) {
            Set<LocationDto> locationDtos = dto.getLocations();
            GroupLocation groupLocation = repository.findByIdOrThrow(entity.getId());
            Set<Location> savedLocations = locationService.saveOrUpdateEventRoutes(locationDtos, groupLocation.getLocations());
            entity.setLocations(savedLocations);
        } else if (crudOperation == CrudOperation.DELETE) {
            GroupLocation groupLocation = repository.findByIdOrThrow(entity.getId());
            Optional.ofNullable(groupLocation.getLocations())
                    .orElse(Collections.emptySet())
                    .stream()
                    .map(Location::getId)
                    .forEach(locationService::delete);
            groupLocation.setLocations(Collections.emptySet());
        }
        return entity;
    }

    @Override
    public String getServiceName() {
        return "groupLocation";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return Map.of("locations", FieldMapper.builder()
                .innerService(locationService)
                .currentServiceField("locations").build());
    }
}
