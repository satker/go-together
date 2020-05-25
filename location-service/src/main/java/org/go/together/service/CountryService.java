package org.go.together.service;

import org.go.together.dto.CountryDto;
import org.go.together.logic.CrudService;
import org.go.together.logic.find.FieldMapper;
import org.go.together.mapper.CountryMapper;
import org.go.together.model.Country;
import org.go.together.repository.CountryRepository;
import org.go.together.validation.CountryValidator;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Service
public class CountryService extends CrudService<CountryDto, Country> {
    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository,
                          CountryMapper countryMapper,
                          CountryValidator countryValidator) {
        super(countryRepository, countryMapper, countryValidator);
        this.countryRepository = countryRepository;
    }

    /*@Override
    public ImmutableMap<String, FunctionToGetValue> getFields() {
        return ImmutableMap.<String, FunctionToGetValue>builder()
                .build();
    }

    @Override
    public String getServiceName() {
        return "country";
    }*/

    public Country findByName(String name) {
        Optional<Country> country = countryRepository.findByName(name);
        return country.orElse(null);
    }

    public Collection<Country> findCountriesLike(String countryName) {
        return countryRepository.findCountriesLike(countryName);
    }

    @Override
    public String getServiceName() {
        return "country";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return null;
    }

    /*private void getCountries() {
        List<Country> inputList = new ArrayList<>();
        try {
            File inputF = new File("/Users/kunats-ay/IdeaProjects/housing-searcher-inc/location-service/src/main/resources/countries.csv");
            InputStream inputFS = new FileInputStream(inputF);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
            AtomicInteger count = new AtomicInteger();
            // skip the header of the csv
            inputList = br.lines().skip(1)
                    .map(city -> city.split(";"))
                    .filter(ci -> ci.length == 2)
                    .map(country -> new Country(country[1], country[0]))
                    .peek(country -> {
                        int c = count.getAndIncrement();
                        if (c % 1000 == 0) {
                            System.out.println(c);
                        }
                    })
                    .collect(Collectors.toList());
            br.close();
        } catch (IOException e) {
        }
        countryRepository.saveAll(inputList);
    }*/
}
