package org.go.together.mapper;

import org.go.together.dto.LocationDto;
import org.go.together.logic.Mapper;
import org.go.together.model.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper implements Mapper<LocationDto, Location> {
    private final PlaceMapper placeMapper;

    public LocationMapper(PlaceMapper placeMapper) {
        this.placeMapper = placeMapper;
    }

    public LocationDto entityToDto(Location location) {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(location.getId());
        locationDto.setAddress(location.getAddress());
        locationDto.setLatitude(location.getLatitude());
        locationDto.setLongitude(location.getLongitude());
        locationDto.setPlace(placeMapper.entityToDto(location.getPlace()));
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
