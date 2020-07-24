package org.go.together.service;

import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.PlaceDto;
import org.go.together.dto.SimpleDto;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.logic.services.CrudService;
import org.go.together.mapper.PlaceMapper;
import org.go.together.model.Country;
import org.go.together.model.Place;
import org.go.together.repository.PlaceRepository;
import org.go.together.validation.PlaceValidator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlaceService extends CrudService<PlaceDto, Place> {
    private final CountryService countryService;
    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;

    public PlaceService(PlaceRepository placeRepository,
                        PlaceMapper placeMapper,
                        PlaceValidator placeValidator,
                        CountryService countryService) {
        super(placeRepository, placeMapper, placeValidator);
        this.placeRepository = placeRepository;
        this.countryService = countryService;
        this.placeMapper = placeMapper;
    }

    public Optional<Place> getLocationEquals(PlaceDto anotherPlaceDto) {
        Place anotherPlace = placeMapper.dtoToEntity(anotherPlaceDto);
        return placeRepository.findLocationByNameAndStateAndByCountryId(anotherPlace.getName(),
                anotherPlace.getState(),
                anotherPlace.getCountry().getId());
    }

    public Place getPlaceById(UUID placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new CannotFindEntityException("Cannot find place by id: " + placeId));
    }

    public Set<SimpleDto> getLocationsByName(String location) {
        String[] split = location.split(",");
        String splitedLocation = split[0].trim().toLowerCase();
        Collection<Place> places;
        if (split.length == 1 && StringUtils.isNotBlank(splitedLocation)) {
            places = placeRepository.findLocationByName(splitedLocation, 0, 5);
        } else if (split.length == 2) {
            String country = split[1].trim();
            Set<UUID> countryIds = countryService.findCountriesLike(country.trim()).stream()
                    .map(Country::getId)
                    .collect(Collectors.toSet());

            places = placeRepository.findLocationByNameAndByCountryId(splitedLocation, countryIds, 0, 5);
        } else {
            places = placeRepository.createQuery().fetchWithPageable(0, 5);
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

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return null;
    }
}
