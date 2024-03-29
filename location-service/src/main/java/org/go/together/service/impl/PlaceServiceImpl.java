package org.go.together.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.go.together.base.CommonCrudService;
import org.go.together.base.Mapper;
import org.go.together.dto.CountryDto;
import org.go.together.dto.PlaceDto;
import org.go.together.dto.SimpleDto;
import org.go.together.model.Country;
import org.go.together.model.Place;
import org.go.together.repository.interfaces.PlaceRepository;
import org.go.together.service.interfaces.CountryService;
import org.go.together.service.interfaces.PlaceService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl extends CommonCrudService<PlaceDto, Place> implements PlaceService {
    private final CountryService countryService;
    private final Mapper<CountryDto, Country> countryMapper;

    @Override
    public Optional<Place> getPlaceEquals(PlaceDto anotherPlaceDto) {
        return ((PlaceRepository) repository).findLocationByNameAndStateAndByCountryId(anotherPlaceDto.getName(),
                anotherPlaceDto.getState(),
                countryMapper.dtoToEntity(anotherPlaceDto.getCountry()).getId());
    }

    @Override
    public Optional<Place> getPlaceByLocationId(UUID locationId) {
        return ((PlaceRepository) repository).findByLocationId(locationId);
    }

    @Override
    public Set<SimpleDto> getLocationsByName(String location) {
        String[] split = location.split(",");
        String splitedLocation = split[0].trim().toLowerCase();
        Collection<Place> places;
        if (split.length == 1 && StringUtils.isNotBlank(splitedLocation)) {
            places = ((PlaceRepository) repository).findLocationByName(splitedLocation, 0, 5);
        } else if (split.length == 2) {
            String country = split[1].trim();
            Set<UUID> countryIds = countryService.findCountriesLike(country.trim()).stream()
                    .map(Country::getId)
                    .collect(Collectors.toSet());

            places = ((PlaceRepository) repository).findLocationByNameAndByCountryId(splitedLocation, countryIds, 0, 5);
        } else {
            places = repository.createQuery().build().fetchWithPageable(0, 5);
        }
        return places.stream()
                .map(loc -> new SimpleDto(loc.getId().toString(), loc.getName() + ", " +
                        loc.getCountry().getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getServiceName() {
        return "places";
    }
}
