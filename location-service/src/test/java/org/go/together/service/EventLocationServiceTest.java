package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.CountryDto;
import org.go.together.dto.EventLocationDto;
import org.go.together.dto.IdDto;
import org.go.together.dto.LocationDto;
import org.go.together.mapper.CountryMapper;
import org.go.together.model.Country;
import org.go.together.model.EventLocation;
import org.go.together.repository.CountryRepository;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = RepositoryContext.class)
class EventLocationServiceTest extends CrudServiceCommonTest<EventLocation, EventLocationDto> {
    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryMapper countryMapper;

    @Test
    void getEventRoute() {
        EventLocationDto eventLocationDto = getCreatedEntityId(dto);
        Set<EventLocationDto> eventRoutes = ((EventLocationService) crudService).getEventRoute(eventLocationDto.getEventId());

        assertEquals(1, eventRoutes.size());
        EventLocationDto eventLocationDtoFound = eventRoutes.iterator().next();

        assertEquals(eventLocationDto, eventLocationDtoFound);
    }

    @Test
    void saveOrUpdateEventRoutes() {
        EventLocationDto eventLocationDto = getCreatedEntityId(dto);
        updatedDto.setId(null);
        updatedDto.setEventId(dto.getEventId());
        Set<EventLocationDto> eventLocations = Set.of(eventLocationDto, updatedDto);

        Set<IdDto> eventRoutes = ((EventLocationService) crudService).saveOrUpdateEventRoutes(eventLocations, dto.getEventId());

        checkEventLocations(eventLocationDto, eventRoutes, 2);
    }

    @Test
    void saveOrUpdateEventRoutesWithDelete() {
        getCreatedEntityId(dto);
        updatedDto.setId(null);
        updatedDto.setEventId(dto.getEventId());
        Set<EventLocationDto> eventLocations = Set.of(updatedDto);

        Set<IdDto> eventRoutes = ((EventLocationService) crudService).saveOrUpdateEventRoutes(eventLocations, dto.getEventId());

        checkEventLocations(updatedDto, eventRoutes, 1);
    }

    private void checkEventLocations(EventLocationDto eventLocationDto, Set<IdDto> eventRoutes, int checkNumber) {
        Set<EventLocationDto> eventLocationSet = eventRoutes.stream()
                .map(IdDto::getId)
                .map(repository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(mapper::entityToDto)
                .peek(eventLocation -> {
                    if (eventLocation.getId().equals(eventLocationDto.getId())) {
                        assertEquals(eventLocationDto, eventLocation);
                    } else {
                        assertEquals(updatedDto, eventLocation);
                    }
                })
                .collect(Collectors.toSet());

        assertEquals(checkNumber, eventLocationSet.size());
    }

    @Override
    protected EventLocationDto createDto() {
        EventLocationDto eventLocationDto = factory.manufacturePojo(EventLocationDto.class);

        LocationDto locationDto = eventLocationDto.getLocation();
        Country country = factory.manufacturePojo(Country.class);
        country.setCountryCode(locationDto.getCountry().getCountryCode().toUpperCase());
        country.setName(locationDto.getCountry().getName().toUpperCase());
        Country savedCountry = countryRepository.save(country);
        CountryDto countryDto = countryMapper.entityToDto(savedCountry);
        locationDto.setCountry(countryDto);

        return eventLocationDto;
    }
}