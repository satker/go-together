package org.go.together.service;

import org.go.together.base.Mapper;
import org.go.together.context.RepositoryContext;
import org.go.together.dto.CountryDto;
import org.go.together.dto.LocationDto;
import org.go.together.dto.PlaceDto;
import org.go.together.enums.CrudOperation;
import org.go.together.model.Country;
import org.go.together.model.Location;
import org.go.together.repository.interfaces.CountryRepository;
import org.go.together.repository.interfaces.PlaceRepository;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = RepositoryContext.class)
class LocationServiceTest extends CrudServiceCommonTest<Location, LocationDto> {
    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private Mapper<CountryDto, Country> countryMapper;

    @Autowired
    private PlaceRepository placeRepository;

    @Override
    @BeforeEach
    public void init() {
        super.init();
        updatedDto.setPlace(dto.getPlace());
    }

    @Override
    protected LocationDto createDto() {
        LocationDto locationDto = factory.manufacturePojo(LocationDto.class);

        PlaceDto placeDto = locationDto.getPlace();
        Country country = factory.manufacturePojo(Country.class);
        country.setCountryCode(placeDto.getCountry().getCountryCode().toUpperCase());
        country.setName(placeDto.getCountry().getName().toUpperCase());
        Country savedCountry = countryRepository.save(country);
        CountryDto countryDto = countryMapper.entityToDto(UUID.randomUUID(), savedCountry);
        placeDto.setId(null);
        placeDto.setCountry(countryDto);
        placeDto.setLocations(Collections.emptySet());

        return locationDto;
    }

    @Override
    protected void checkDtos(LocationDto dto, LocationDto savedObject, CrudOperation operation) {
        assertEquals(dto.getIsEnd(), savedObject.getIsEnd());
        assertEquals(dto.getIsStart(), savedObject.getIsStart());
        assertEquals(dto.getAddress(), savedObject.getAddress());
        assertEquals(dto.getRouteNumber(), savedObject.getRouteNumber());
        assertEquals(dto.getLatitude(), savedObject.getLatitude());
        assertEquals(dto.getLongitude(), savedObject.getLongitude());
        assertEquals(dto.getPlace().getLocations(), savedObject.getPlace().getLocations());
        assertEquals(dto.getPlace().getCountry(), savedObject.getPlace().getCountry());
        assertEquals(dto.getPlace().getState(), savedObject.getPlace().getState());
        assertEquals(dto.getPlace().getName(), savedObject.getPlace().getName());
        assertEquals(repository.findAll().size(), placeRepository.findAll().size());
    }
}