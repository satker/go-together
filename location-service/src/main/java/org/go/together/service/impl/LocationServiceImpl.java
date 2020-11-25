package org.go.together.service.impl;

import lombok.RequiredArgsConstructor;
import org.go.together.base.CommonCrudService;
import org.go.together.base.Mapper;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.IdDto;
import org.go.together.dto.LocationDto;
import org.go.together.dto.PlaceDto;
import org.go.together.enums.CrudOperation;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.model.Location;
import org.go.together.model.Place;
import org.go.together.service.interfaces.LocationService;
import org.go.together.service.interfaces.PlaceService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl extends CommonCrudService<LocationDto, Location> implements LocationService {
    private final PlaceService placeService;
    private final Mapper<PlaceDto, Place> placeMapper;

    public Set<Location> saveOrUpdateEventRoutes(UUID requestId, Set<LocationDto> locationDtos, Set<Location> presentedLocations) {
        Set<UUID> presentedEventLocations = presentedLocations.stream()
                .map(Location::getId)
                .collect(Collectors.toSet());
        Set<IdDto> result = new HashSet<>();

        presentedEventLocations.stream()
                .filter(eventLocationId -> locationDtos.stream()
                        .noneMatch(eventLocationDto -> eventLocationId.equals(eventLocationDto.getId())))
                .forEach(eventLocationId -> super.delete(requestId, eventLocationId));

        locationDtos
                .forEach(eventLocationDtoEntry -> {
                    if (presentedEventLocations.contains(eventLocationDtoEntry.getId())) {
                        result.add(super.update(requestId, eventLocationDtoEntry));
                    } else {
                        result.add(super.create(requestId, eventLocationDtoEntry));
                    }
                });

        return result.stream()
                .map(IdDto::getId)
                .map(repository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    @Override
    protected Location enrichEntity(UUID requestId, Location entity, LocationDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.CREATE) {
            PlaceDto placeDto = dto.getPlace();
            Optional<Place> placeEquals = placeService.getPlaceEquals(placeDto);

            Optional<Place> placeByLocationId = placeService.getPlaceByLocationId(entity.getId());
            if (placeByLocationId.isPresent() && (placeEquals.isEmpty()
                    || placeByLocationId.get().getId().equals(placeEquals.get().getId()))) {
                removePlaceByLocationId(requestId, entity);
            }

            if (placeEquals.isEmpty()) {
                placeDto.setLocations(Collections.singleton(entity.getId()));
                placeService.create(requestId, placeDto);
            } else {
                Place place = placeEquals.get();
                Set<Location> locations = place.getLocations();
                locations.add(entity);
                PlaceDto placeUpdated = placeMapper.entityToDto(place);
                placeService.update(requestId, placeUpdated);
            }
        } else if (crudOperation == CrudOperation.DELETE) {
            removePlaceByLocationId(requestId, entity);
        }
        return entity;
    }

    private void removePlaceByLocationId(UUID requestId, Location entity) {
        Place place = placeService.getPlaceByLocationId(entity.getId())
                .orElseThrow(() -> new CannotFindEntityException("Cannot find place by id: " + entity.getId()));
        if (place.getLocations().stream()
                .allMatch(location -> location.getId().equals(entity.getId()))) {
            placeService.delete(requestId, place.getId());
        } else {
            place.setLocations(place.getLocations().stream()
                    .filter(location -> !location.getId().equals(entity.getId()))
                    .collect(Collectors.toSet()));
            PlaceDto placeUpdated = placeMapper.entityToDto(place);
            placeService.update(requestId, placeUpdated);
        }
    }

    @Override
    public String getServiceName() {
        return "locations";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return Map.of("isStart", FieldMapper.builder()
                        .currentServiceField("isStart")
                        .fieldClass(Boolean.class).build(),
                "isEnd", FieldMapper.builder()
                        .currentServiceField("isEnd")
                        .fieldClass(Boolean.class).build(),
                "latitude,longitude", FieldMapper.builder()
                        .currentServiceField("latitude,longitude").build());
    }
}
