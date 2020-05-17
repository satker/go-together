package org.go.together.service;

import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.LocationDto;
import org.go.together.dto.SimpleDto;
import org.go.together.logic.CrudService;
import org.go.together.mapper.LocationMapper;
import org.go.together.model.Country;
import org.go.together.model.Location;
import org.go.together.repository.LocationRepository;
import org.go.together.validation.LocationValidator;
import org.springframework.stereotype.Service;

import java.util.Collection;
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
            String country = split[1].split(" ")[0];
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

    /*@Override
    public ImmutableMap<String, FunctionToGetValue> getFields() {
        return ImmutableMap.<String, FunctionToGetValue>builder()
                .build();
    }

    @Override
    public String getServiceName() {
        return "city";
    }

     private void getCitiesBuffer() {
        List<City> inputList = new ArrayList<>();
        try {
            File inputF = new File("/Users/kunats-ay/IdeaProjects/housing-searcher-inc/location-service/src/main/resources/GEODATASOURCE-CITIES-FREE.TXT");
            InputStream inputFS = new FileInputStream(inputF);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
            Map<String, String> countryIdMap = new HashMap<>();
            countryRepository.findAll().forEach(country -> countryIdMap.put(country.getCountryCode(), country.getId()));
            AtomicInteger count = new AtomicInteger();
            // skip the header of the csv
            inputList = br.lines().skip(1)
                    .map(city -> city.split("\t"))
                    .filter(ci -> ci.length == 3)
                    .map(city -> new City(city[2], countryIdMap.get(city[1])))
                    .peek(city -> {
                        int c = count.getAndIncrement();
                        if (c % 1000 == 0) {
                            System.out.println(c);
                        }
                    })
                    .collect(Collectors.toList());
            br.close();
        } catch (IOException e) {
        }

        List<City> cities = new ArrayList<>();
        for (int i = 0; i < inputList.size(); i++) {
            cities.add(inputList.get(i));
            if (i % 4000 == 0) {
                cityRepository.saveAll(cities);
                cities.clear();
                System.out.println("saving city" + i);
            }
        }
        if (!cities.isEmpty()) {
            cityRepository.saveAll(cities);
        }
    }*/
}
