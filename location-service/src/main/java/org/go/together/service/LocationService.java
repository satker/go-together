package org.go.together.service;

import com.google.common.collect.ImmutableMap;
import org.go.together.dto.IdDto;
import org.go.together.dto.LocationDto;
import org.go.together.dto.PlaceDto;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.enums.CrudOperation;
import org.go.together.logic.services.CrudService;
import org.go.together.mapper.LocationMapper;
import org.go.together.model.Location;
import org.go.together.model.Place;
import org.go.together.repository.LocationRepository;
import org.go.together.validation.LocationValidator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LocationService extends CrudService<LocationDto, Location> {
    private final LocationMapper locationMapper;
    private final LocationRepository locationRepository;
    private final PlaceService placeService;

    public LocationService(LocationRepository locationRepository,
                           LocationMapper locationMapper,
                           LocationValidator locationValidator,
                           PlaceService placeService) {
        super(locationRepository, locationMapper, locationValidator);
        this.locationMapper = locationMapper;
        this.locationRepository = locationRepository;
        this.placeService = placeService;
    }

    public Set<LocationDto> getEventRoute(UUID eventId) {
        return locationRepository.findByEventId(eventId).stream()
                .map(locationMapper::entityToDto)
                .collect(Collectors.toSet());
    }

    public Set<Location> saveOrUpdateEventRoutes(Set<LocationDto> locationDtos, Set<Location> presentedLocations) {
        Set<UUID> presentedEventLocations = presentedLocations.stream()
                .map(Location::getId)
                .collect(Collectors.toSet());
        Set<IdDto> result = new HashSet<>();

        presentedEventLocations.stream()
                .filter(eventLocationId -> locationDtos.stream()
                        .noneMatch(eventLocationDto -> eventLocationId.equals(eventLocationDto.getId())))
                .forEach(super::delete);

        locationDtos
                .forEach(eventLocationDtoEntry -> {
                    if (presentedEventLocations.contains(eventLocationDtoEntry.getId())) {
                        result.add(super.update(eventLocationDtoEntry));
                    } else {
                        result.add(super.create(eventLocationDtoEntry));
                    }
                });

        return result.stream()
                .map(IdDto::getId)
                .map(locationRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    @Override
    protected Location enrichEntity(Location entity, LocationDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.UPDATE || crudOperation == CrudOperation.CREATE) {
            PlaceDto location = dto.getPlace();
            Optional<Place> locationEquals = placeService.getLocationEquals(location);
            if (locationEquals.isEmpty()) {
                IdDto idDto = placeService.create(location);
                entity.setPlace(placeService.getPlaceById(idDto.getId()));
            } else {
                entity.setPlace(locationEquals.get());
            }
        } else if (crudOperation == CrudOperation.DELETE) {
            Place place = entity.getPlace();
            Number countPlaceIdRows = locationRepository.getCountPlaceIdRows(place.getId());
            if (countPlaceIdRows.intValue() - 1 == 0) {
                placeService.delete(place.getId());
            }
        }
        return entity;
    }

    @Override
    public String getServiceName() {
        return "eventLocation";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return ImmutableMap.<String, FieldMapper>builder()
                .put("isStart", FieldMapper.builder()
                        .currentServiceField("isStart").build())
                .put("isEnd", FieldMapper.builder()
                        .currentServiceField("isEnd").build())
                .put("latitude,longitude", FieldMapper.builder()
                        .currentServiceField("latitude,longitude").build()).build();
    }
}
