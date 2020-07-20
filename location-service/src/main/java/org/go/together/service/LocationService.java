package org.go.together.service;

import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.LocationDto;
import org.go.together.dto.SimpleDto;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.logic.services.CrudService;
import org.go.together.mapper.LocationMapper;
import org.go.together.model.Country;
import org.go.together.model.Location;
import org.go.together.repository.LocationRepository;
import org.go.together.validation.LocationValidator;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LocationService extends CrudService<LocationDto, Location> {
    private final CountryService countryService;
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository,
                           LocationMapper locationMapper,
                           LocationValidator locationValidator,
                           CountryService countryService) {
        super(locationRepository, locationMapper, locationValidator);
        this.locationRepository = locationRepository;
        this.countryService = countryService;
    }

    public Set<SimpleDto> getLocationsByName(String location) {
        String[] split = location.split(",");
        String splitedLocation = split[0].trim().toLowerCase();
        Collection<Location> locations;
        if (split.length == 1 && StringUtils.isNotBlank(splitedLocation)) {
            locations = locationRepository.findLocationByName(splitedLocation, 0, 5);
        } else if (split.length == 2) {
            String country = split[1].trim();
            Set<UUID> countryIds = countryService.findCountriesLike(country.trim()).stream()
                    .map(Country::getId)
                    .collect(Collectors.toSet());

            locations = locationRepository.findLocationByNameAndByCountryId(splitedLocation, countryIds, 0, 5);
        } else {
            locations = locationRepository.createQuery().fetchWithPageable(0, 5);
        }
        return locations.stream()
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
