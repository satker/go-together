package org.go.together.service;

import org.go.together.base.Mapper;
import org.go.together.context.RepositoryContext;
import org.go.together.dto.*;
import org.go.together.enums.CrudOperation;
import org.go.together.model.Country;
import org.go.together.model.GroupLocation;
import org.go.together.repository.interfaces.CountryRepository;
import org.go.together.repository.interfaces.LocationRepository;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;


@ContextConfiguration(classes = RepositoryContext.class)
class GroupLocationServiceTest extends CrudServiceCommonTest<GroupLocation, GroupLocationDto> {
    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private Mapper<CountryDto, Country> countryMapper;

    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void updateSimilarLocationTest() {
        GroupLocationDto createdDto = getCreatedEntityId(dto);
        updatedDto.setId(createdDto.getId());
        Set<LocationDto> locationsUpdated = createdDto.getLocations();
        Set<LocationDto> locations = updatedDto.getLocations().stream()
                .limit(2)
                .peek(location -> location.setRouteNumber(location.getRouteNumber() + locationsUpdated.size()))
                .collect(Collectors.toSet());
        locationsUpdated.addAll(locations);
        updatedDto.setLocations(locationsUpdated);
        IdDto update = crudService.update(updatedDto);
        GroupLocationDto savedUpdatedObject = crudService.read(update.getId());
        checkDtos(updatedDto, savedUpdatedObject, CrudOperation.UPDATE);
    }

    @Test
    public void deleteLocationsTest() {
        GroupLocationDto createdDto = getCreatedEntityId(dto);
        updatedDto.setId(createdDto.getId());
        Set<LocationDto> locations = updatedDto.getLocations().stream()
                .limit(2)
                .collect(Collectors.toSet());
        Set<Integer> numbers = IntStream.rangeClosed(1, locations.size())
                .boxed()
                .collect(Collectors.toSet());
        Iterator<LocationDto> iterator = locations.iterator();
        for (Integer number : numbers) {
            LocationDto eventLocationDto = iterator.next();
            eventLocationDto.setRouteNumber(number);
            enrichWithCorrectEndStartRoute(locations.size(), number, eventLocationDto);
        }
        updatedDto.setLocations(locations);
        IdDto update = crudService.update(updatedDto);
        GroupLocationDto savedUpdatedObject = crudService.read(update.getId());
        checkDtos(updatedDto, savedUpdatedObject, CrudOperation.UPDATE);
    }

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
            enrichWithCorrectEndStartRoute(locations.size(), number, eventLocationDto);
        }

        return locationDto;
    }

    private void enrichWithCorrectEndStartRoute(int locationsSize, Integer number, LocationDto eventLocationDto) {
        if (number == 1) {
            eventLocationDto.setIsStart(true);
            eventLocationDto.setIsEnd(false);
        } else if (number == locationsSize) {
            eventLocationDto.setIsStart(false);
            eventLocationDto.setIsEnd(true);
        } else {
            eventLocationDto.setIsStart(false);
            eventLocationDto.setIsEnd(false);
        }
    }

    @Override
    protected void checkDtos(GroupLocationDto dto, GroupLocationDto savedObject, CrudOperation operation) {
        assertEquals(dto.getCategory(), savedObject.getCategory());
        assertEquals(dto.getGroupId(), savedObject.getGroupId());

        Map<Number, LocationDto> oneMap = dto.getLocations().stream()
                .collect(Collectors.toMap(LocationDto::getRouteNumber, Function.identity()));

        Map<Number, LocationDto> anotherMap = savedObject.getLocations().stream()
                .collect(Collectors.toMap(LocationDto::getRouteNumber, Function.identity()));

        oneMap.forEach((key, value) -> compareLocationDto(value, anotherMap.get(key)));

        int sumLocations = repository.findAll().stream()
                .map(GroupLocation::getLocations)
                .mapToInt(Collection::size)
                .sum();
        assertEquals(sumLocations, locationRepository.findAll().size());
    }

    private void compareLocationDto(LocationDto oneLocationDto, LocationDto anotherLocationDto) {
        assertEquals(oneLocationDto.getIsEnd(), anotherLocationDto.getIsEnd());
        assertEquals(oneLocationDto.getIsStart(), anotherLocationDto.getIsStart());
        assertEquals(oneLocationDto.getAddress(), anotherLocationDto.getAddress());
        assertEquals(oneLocationDto.getRouteNumber(), anotherLocationDto.getRouteNumber());
        assertEquals(oneLocationDto.getLatitude(), anotherLocationDto.getLatitude());
        assertEquals(oneLocationDto.getLongitude(), anotherLocationDto.getLongitude());
        assertEquals(oneLocationDto.getPlace().getLocations(), anotherLocationDto.getPlace().getLocations());
        assertEquals(oneLocationDto.getPlace().getCountry(), anotherLocationDto.getPlace().getCountry());
        assertEquals(oneLocationDto.getPlace().getState(), anotherLocationDto.getPlace().getState());
        assertEquals(oneLocationDto.getPlace().getName(), anotherLocationDto.getPlace().getName());
    }
}