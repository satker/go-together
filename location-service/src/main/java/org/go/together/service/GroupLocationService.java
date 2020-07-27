package org.go.together.service;

import com.google.common.collect.ImmutableMap;
import org.go.together.dto.GroupLocationDto;
import org.go.together.dto.LocationCategory;
import org.go.together.dto.LocationDto;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
            Set<LocationDto> locationDtos = dto.getCategory() == LocationCategory.EVENT ?
                    prepareLocations(dto) : dto.getLocations();
            Set<Location> savedLocations = locationService.saveOrUpdateEventRoutes(locationDtos, Collections.emptySet());
            entity.setLocations(savedLocations);
        } else if (crudOperation == CrudOperation.UPDATE) {
            Set<LocationDto> locationDtos = dto.getCategory() == LocationCategory.EVENT ?
                    prepareLocations(dto) : dto.getLocations();
            GroupLocation groupLocation = groupLocationRepository.findById(entity.getId())
                    .orElseThrow(() -> new CannotFindEntityException("Cannot find group location by id: " + entity.getId()));
            Set<Location> savedLocations = locationService.saveOrUpdateEventRoutes(locationDtos, groupLocation.getLocations());
            entity.setLocations(savedLocations);
        } else if (crudOperation == CrudOperation.DELETE) {
            GroupLocation groupLocation = groupLocationRepository.findById(entity.getId())
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

    private Set<LocationDto> prepareLocations(GroupLocationDto dto) {
        int size = dto.getLocations().size();
        return dto.getLocations().stream()
                .peek(locationDto -> {
                    if (locationDto.getRouteNumber() == 1) {
                        locationDto.setIsEnd(false);
                        locationDto.setIsStart(true);
                    } else if (locationDto.getRouteNumber() == size) {
                        locationDto.setIsEnd(true);
                        locationDto.setIsStart(false);
                    } else {
                        locationDto.setIsEnd(false);
                        locationDto.setIsStart(false);
                    }
                }).collect(Collectors.toSet());
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
