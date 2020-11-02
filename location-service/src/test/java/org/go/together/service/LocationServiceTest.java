package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.CountryDto;
import org.go.together.dto.LocationDto;
import org.go.together.dto.PlaceDto;
import org.go.together.enums.CrudOperation;
import org.go.together.mapper.Mapper;
import org.go.together.model.Country;
import org.go.together.model.Location;
import org.go.together.notification.streams.NotificationSource;
import org.go.together.repository.interfaces.CountryRepository;
import org.go.together.repository.interfaces.PlaceRepository;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = RepositoryContext.class)
class LocationServiceTest extends CrudServiceCommonTest<Location, LocationDto> {
    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private Mapper<CountryDto, Country> countryMapper;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private NotificationSource source;

    @Override
    @BeforeEach
    public void init() {
        super.init();
        MessageChannel messageChannel = Mockito.mock(MessageChannel.class);
        when(source.output()).thenReturn(messageChannel);
        when(messageChannel.send(any())).thenReturn(true);
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
        CountryDto countryDto = countryMapper.entityToDto(savedCountry);
        placeDto.setId(null);
        placeDto.setCountry(countryDto);
        placeDto.setLocations(Collections.emptySet());

        return locationDto;
    }

    @Override
    protected void checkDtos(LocationDto dto, LocationDto savedObject, CrudOperation operation) {
        super.checkDtos(dto, savedObject, operation);
        assertEquals(repository.findAll().size(), placeRepository.findAll().size());
    }
}