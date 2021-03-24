package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.CommonMapper;
import org.go.together.base.Mapper;
import org.go.together.dto.CountryDto;
import org.go.together.dto.PlaceDto;
import org.go.together.model.Country;
import org.go.together.model.Location;
import org.go.together.model.Place;
import org.go.together.repository.interfaces.LocationRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PlaceMapper extends CommonMapper<PlaceDto, Place> {
    private final Mapper<CountryDto, Country> countryMapper;
    private final LocationRepository locationRepository;

    public PlaceDto toDto(Place place) {
        PlaceDto placeDto = new PlaceDto();
        placeDto.setCountry(countryMapper.entityToDto(place.getCountry()));
        placeDto.setId(place.getId());
        placeDto.setState(place.getState());
        placeDto.setName(place.getName());
        placeDto.setLocations(place.getLocations().stream()
                .map(Location::getId)
                .collect(Collectors.toSet()));
        return placeDto;
    }

    public Place toEntity(PlaceDto placeDto) {
        Place place = new Place();
        place.setId(placeDto.getId());
        Country country = countryMapper.dtoToEntity(placeDto.getCountry());
        place.setCountry(country);
        place.setName(placeDto.getName());
        place.setState(placeDto.getState());
        place.setLocations(placeDto.getLocations().stream()
                .map(locationRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet()));
        return place;
    }
}
