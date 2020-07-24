package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.CountryDto;
import org.go.together.dto.GroupLocationDto;
import org.go.together.dto.LocationDto;
import org.go.together.dto.PlaceDto;
import org.go.together.enums.CrudOperation;
import org.go.together.mapper.CountryMapper;
import org.go.together.model.Country;
import org.go.together.model.GroupLocation;
import org.go.together.repository.CountryRepository;
import org.go.together.tests.CrudServiceCommonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@ContextConfiguration(classes = RepositoryContext.class)
class GroupLocationServiceTest extends CrudServiceCommonTest<GroupLocation, GroupLocationDto> {
    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryMapper countryMapper;

    @Override
    protected GroupLocationDto createDto() {
        GroupLocationDto locationDto = factory.manufacturePojo(GroupLocationDto.class);
        Collection<LocationDto> locations = locationDto.getLocations();
        Set<Integer> numbers = IntStream.rangeClosed(1, locations.size())
                .boxed()
                .collect(Collectors.toSet());
        Iterator<LocationDto> iterator = locations.iterator();
        for (Integer number : numbers) {
            LocationDto eventLocationDto = iterator.next();
            eventLocationDto.setRouteNumber(number);
            PlaceDto placeDto = eventLocationDto.getPlace();
            Country country = factory.manufacturePojo(Country.class);
            country.setCountryCode(placeDto.getCountry().getCountryCode().toUpperCase());
            country.setName(placeDto.getCountry().getName().toUpperCase());
            Country savedCountry = countryRepository.save(country);
            CountryDto countryDto = countryMapper.entityToDto(savedCountry);
            placeDto.setId(null);
            placeDto.setCountry(countryDto);
            if (number == 1) {
                eventLocationDto.setIsStart(true);
                eventLocationDto.setIsEnd(false);
            } else if (number == locations.size()) {
                eventLocationDto.setIsStart(false);
                eventLocationDto.setIsEnd(true);
            } else {
                eventLocationDto.setIsStart(false);
                eventLocationDto.setIsEnd(false);
            }
        }

        return locationDto;
    }

    @Override
    protected void checkDtos(GroupLocationDto dto, GroupLocationDto savedObject, CrudOperation operation) {
        assertEquals(dto.getCategory(), savedObject.getCategory());
        assertEquals(dto.getGroupId(), savedObject.getGroupId());
        assertTrue(savedObject.getLocations().containsAll(dto.getLocations()));
    }
}