package org.go.together.service;

import com.google.common.collect.ImmutableMap;
import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.dto.GroupLocationDto;
import org.go.together.dto.LocationDto;
import org.go.together.enums.CrudOperation;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.find.dto.FieldMapper;
import org.go.together.model.GroupLocation;
import org.go.together.model.Location;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class GroupLocationService extends CrudServiceImpl<GroupLocationDto, GroupLocation> {
    private final LocationService locationService;

    protected GroupLocationService(LocationService locationService) {
        this.locationService = locationService;
    }

    @Override
    protected GroupLocation enrichEntity(GroupLocation entity, GroupLocationDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.CREATE) {
            Set<LocationDto> locationDtos = dto.getLocations();
            Set<Location> savedLocations = locationService.saveOrUpdateEventRoutes(locationDtos, Collections.emptySet());
            entity.setLocations(savedLocations);
        } else if (crudOperation == CrudOperation.UPDATE) {
            Set<LocationDto> locationDtos = dto.getLocations();
            GroupLocation groupLocation = repository.findById(entity.getId())
                    .orElseThrow(() -> new CannotFindEntityException("Cannot find group location by id: " + entity.getId()));
            Set<Location> savedLocations = locationService.saveOrUpdateEventRoutes(locationDtos, groupLocation.getLocations());
            entity.setLocations(savedLocations);
        } else if (crudOperation == CrudOperation.DELETE) {
            GroupLocation groupLocation = repository.findById(entity.getId())
                    .orElseThrow(() -> new CannotFindEntityException("Cannot find group location by id: " + entity.getId()));
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
        return ImmutableMap.<String, FieldMapper>builder()
                .put("locations", FieldMapper.builder()
                        .innerService(locationService)
                        .currentServiceField("locations").build()).build();
    }
}
