package org.go.together.mapper;

import org.go.together.dto.PlaceDto;
import org.go.together.logic.Mapper;
import org.go.together.model.Country;
import org.go.together.model.Place;
import org.springframework.stereotype.Component;

@Component
public class PlaceMapper implements Mapper<PlaceDto, Place> {
    private final CountryMapper countryMapper;

    public PlaceMapper(CountryMapper countryMapper) {
        this.countryMapper = countryMapper;
    }

    public PlaceDto entityToDto(Place place) {
        PlaceDto placeDto = new PlaceDto();
        placeDto.setCountry(countryMapper.entityToDto(place.getCountry()));
        placeDto.setId(place.getId());
        placeDto.setState(place.getState());
        placeDto.setName(place.getName());
        return placeDto;
    }

    public Place dtoToEntity(PlaceDto placeDto) {
        Place place = new Place();
        place.setId(placeDto.getId());
        Country country = countryMapper.dtoToEntity(placeDto.getCountry());
        place.setCountry(country);
        place.setName(placeDto.getName());
        place.setState(placeDto.getState());
        return place;
    }
}
