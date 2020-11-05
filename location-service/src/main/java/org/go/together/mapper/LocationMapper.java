package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.Mapper;
import org.go.together.dto.LocationDto;
import org.go.together.dto.PlaceDto;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.model.Location;
import org.go.together.model.Place;
import org.go.together.repository.interfaces.PlaceRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationMapper implements Mapper<LocationDto, Location> {
    private final PlaceRepository placeRepository;
    private final Mapper<PlaceDto, Place> placeMapper;

    public LocationDto entityToDto(Location location) {
        Place place = placeRepository.findByLocationId(location.getId())
                .orElseThrow(() -> new CannotFindEntityException("Cannot find place by location id " + location.getId()));
        LocationDto locationDto = new LocationDto();
        locationDto.setId(location.getId());
        locationDto.setAddress(location.getAddress());
        locationDto.setLatitude(location.getLatitude());
        locationDto.setLongitude(location.getLongitude());
        locationDto.setPlace(placeMapper.entityToDto(place));
        locationDto.setRouteNumber(location.getRouteNumber());
        locationDto.setIsEnd(location.getIsEnd());
        locationDto.setIsStart(location.getIsStart());
        return locationDto;
    }

    public Location dtoToEntity(LocationDto locationDto) {
        Location location = new Location();
        location.setId(locationDto.getId());
        location.setAddress(locationDto.getAddress());
        location.setLatitude(locationDto.getLatitude());
        location.setLongitude(locationDto.getLongitude());
        location.setRouteNumber(locationDto.getRouteNumber());
        location.setIsEnd(locationDto.getIsEnd());
        location.setIsStart(locationDto.getIsStart());
        return location;
    }
}
