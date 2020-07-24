package org.go.together.service;

import org.go.together.dto.GroupLocationDto;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.enums.CrudOperation;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.logic.services.CrudService;
import org.go.together.mapper.GroupLocationMapper;
import org.go.together.model.GroupLocation;
import org.go.together.model.Location;
import org.go.together.repository.GroupLocationRepository;
import org.go.together.validation.GroupLocationValidator;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Service
public class GroupLocationService extends CrudService<GroupLocationDto, GroupLocation> {
    private final LocationService locationService;
    private final GroupLocationRepository groupLocationRepository;

    protected GroupLocationService(GroupLocationRepository groupLocationRepository,
                                   GroupLocationMapper groupLocationMapper,
                                   GroupLocationValidator groupLocationValidator,
                                   LocationService locationService) {
        super(groupLocationRepository, groupLocationMapper, groupLocationValidator);
        this.locationService = locationService;
        this.groupLocationRepository = groupLocationRepository;
    }

    @Override
    protected GroupLocation enrichEntity(GroupLocation entity, GroupLocationDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.CREATE) {
            Set<Location> savedLocations = locationService.saveOrUpdateEventRoutes(dto.getLocations(), Collections.emptySet());
            entity.setLocations(savedLocations);
        } else if (crudOperation == CrudOperation.UPDATE) {
            GroupLocation groupLocation = groupLocationRepository.findById(entity.getId())
                    .orElseThrow(() -> new CannotFindEntityException("Cannot find group location by id: " + entity.getId()));
            Set<Location> savedLocations = locationService.saveOrUpdateEventRoutes(dto.getLocations(), groupLocation.getLocations());
            entity.setLocations(savedLocations);
        } else if (crudOperation == CrudOperation.DELETE) {
            GroupLocation groupLocation = groupLocationRepository.findById(entity.getId())
                    .orElseThrow(() -> new CannotFindEntityException("Cannot find group location by id: " + entity.getId()));
            groupLocation.getLocations().stream()
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
        return null;
    }
}
