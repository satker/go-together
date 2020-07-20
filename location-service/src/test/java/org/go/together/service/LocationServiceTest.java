package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.CountryDto;
import org.go.together.dto.LocationDto;
import org.go.together.dto.SimpleDto;
import org.go.together.mapper.CountryMapper;
import org.go.together.model.Country;
import org.go.together.model.Location;
import org.go.together.repository.CountryRepository;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = RepositoryContext.class)
class LocationServiceTest extends CrudServiceCommonTest<Location, LocationDto> {
    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryMapper countryMapper;

    @Test
    void getLocationsByName() {
        LocationDto locationDto = getCreatedEntityId(dto);
        Set<SimpleDto> locationsByName = ((LocationService) crudService).getLocationsByName(locationDto.getName().substring(0, 3));

        assertEquals(1, locationsByName.size());
        String name = locationsByName.iterator().next().getName();
        String expectedName = locationDto.getName() + ", " + locationDto.getCountry().getName();
        assertEquals(expectedName, name);

    }

    @Test
    void getLocationsByNameAndCountry() {
        LocationDto locationDto = getCreatedEntityId(dto);
        String search = locationDto.getName().substring(0, 3) + ", " + locationDto.getCountry().getName().substring(0, 3);
        Set<SimpleDto> locationsByName = ((LocationService) crudService).getLocationsByName(search);

        assertEquals(1, locationsByName.size());
        String name = locationsByName.iterator().next().getName();
        String expectedName = locationDto.getName() + ", " + locationDto.getCountry().getName();
        assertEquals(expectedName, name);

    }

    @Override
    protected LocationDto createDto() {
        LocationDto locationDto = factory.manufacturePojo(LocationDto.class);

        Country country = factory.manufacturePojo(Country.class);
        country.setCountryCode(locationDto.getCountry().getCountryCode().toUpperCase());
        country.setName(locationDto.getCountry().getName().toUpperCase());
        Country savedCountry = countryRepository.save(country);
        CountryDto countryDto = countryMapper.entityToDto(savedCountry);
        locationDto.setCountry(countryDto);

        return locationDto;
    }
}