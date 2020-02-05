package org.go.together.mapper;

import org.go.together.dto.LocationDto;
import org.go.together.interfaces.Mapper;
import org.go.together.model.Country;
import org.go.together.model.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper implements Mapper<LocationDto, Location> {
    private final CountryMapper countryMapper;

    public LocationMapper(CountryMapper countryMapper) {
        this.countryMapper = countryMapper;
    }

    public LocationDto entityToDto(Location location) {
        LocationDto locationDto = new LocationDto();
        locationDto.setCountry(countryMapper.entityToDto(location.getCountry()));
        locationDto.setId(location.getId());
        locationDto.setState(location.getState());
        locationDto.setName(location.getName());
        return locationDto;
    }

    public Location dtoToEntity(LocationDto locationDto) {
        Location location = new Location();
        Country country = countryMapper.dtoToEntity(locationDto.getCountry());
        location.setId(location.getId());
        location.setCountry(country);
        location.setName(locationDto.getName());
        location.setState(locationDto.getState());
        return location;
    }
}
