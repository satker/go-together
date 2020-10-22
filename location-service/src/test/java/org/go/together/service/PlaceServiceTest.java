package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.CountryDto;
import org.go.together.dto.PlaceDto;
import org.go.together.dto.SimpleDto;
import org.go.together.mapper.Mapper;
import org.go.together.model.Country;
import org.go.together.model.Place;
import org.go.together.repository.interfaces.CountryRepository;
import org.go.together.service.interfaces.PlaceService;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = RepositoryContext.class)
class PlaceServiceTest extends CrudServiceCommonTest<Place, PlaceDto> {
    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private Mapper<CountryDto, Country> countryMapper;

    @Test
    void getLocationsByName() {
        PlaceDto placeDto = getCreatedEntityId(dto);
        Set<SimpleDto> locationsByName = ((PlaceService) crudService).getLocationsByName(placeDto.getName().substring(0, 3));

        assertEquals(1, locationsByName.size());
        String name = locationsByName.iterator().next().getName();
        String expectedName = placeDto.getName() + ", " + placeDto.getCountry().getName();
        assertEquals(expectedName, name);

    }

    @Test
    void getLocationsByNameAndCountry() {
        PlaceDto placeDto = getCreatedEntityId(dto);
        String search = placeDto.getName().substring(0, 3) + ", " + placeDto.getCountry().getName().substring(0, 3);
        Set<SimpleDto> locationsByName = ((PlaceService) crudService).getLocationsByName(search);

        assertEquals(1, locationsByName.size());
        String name = locationsByName.iterator().next().getName();
        String expectedName = placeDto.getName() + ", " + placeDto.getCountry().getName();
        assertEquals(expectedName, name);

    }

    @Override
    protected PlaceDto createDto() {
        PlaceDto placeDto = factory.manufacturePojo(PlaceDto.class);

        Country country = factory.manufacturePojo(Country.class);
        country.setCountryCode(placeDto.getCountry().getCountryCode().toUpperCase());
        country.setName(placeDto.getCountry().getName().toUpperCase());
        Country savedCountry = countryRepository.save(country);
        CountryDto countryDto = countryMapper.entityToDto(savedCountry);
        placeDto.setCountry(countryDto);

        placeDto.setLocations(Collections.emptySet());

        return placeDto;
    }
}