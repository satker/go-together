package org.go.together.service;

import org.go.together.dto.LocationDto;
import org.go.together.logic.CrudService;
import org.go.together.mapper.LocationMapper;
import org.go.together.model.Location;
import org.go.together.repository.LocationRepository;
import org.go.together.validation.LocationValidator;
import org.springframework.stereotype.Service;

@Service
public class LocationService extends CrudService<LocationDto, Location> {
    public LocationService(LocationRepository locationRepository,
                           LocationMapper locationMapper,
                           LocationValidator locationValidator,
                           CountryService countryService) {
        super(locationRepository, locationMapper, locationValidator);
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
