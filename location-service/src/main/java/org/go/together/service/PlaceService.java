package org.go.together.service;

import org.apache.commons.lang3.StringUtils;
import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.dto.PlaceDto;
import org.go.together.dto.SimpleDto;
import org.go.together.mapper.CountryMapper;
import org.go.together.model.Country;
import org.go.together.model.Place;
import org.go.together.repository.PlaceRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PlaceService extends CrudServiceImpl<PlaceDto, Place> {
    private final CountryService countryService;
    private final CountryMapper countryMapper;

    public PlaceService(CountryService countryService,
                        CountryMapper countryMapper) {
        this.countryService = countryService;
        this.countryMapper = countryMapper;
    }

    public Optional<Place> getPlaceEquals(PlaceDto anotherPlaceDto) {
        return ((PlaceRepository) repository).findLocationByNameAndStateAndByCountryId(anotherPlaceDto.getName(),
                anotherPlaceDto.getState(),
                countryMapper.dtoToEntity(anotherPlaceDto.getCountry()).getId());
    }

    public Optional<Place> getPlaceByLocationId(UUID locationId) {
        return ((PlaceRepository) repository).findByLocationId(locationId);
    }

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
            places = repository.createQuery().fetchWithPageable(0, 5);
        }
        return places.stream()
                .map(loc -> new SimpleDto(loc.getId().toString(), loc.getName() + ", " +
                        loc.getCountry().getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getServiceName() {
        return "location";
    }
}
