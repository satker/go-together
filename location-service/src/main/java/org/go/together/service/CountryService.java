package org.go.together.service;

import org.go.together.dto.CountryDto;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.logic.services.CrudService;
import org.go.together.mapper.CountryMapper;
import org.go.together.model.Country;
import org.go.together.repository.CountryRepository;
import org.go.together.validation.CountryValidator;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public class CountryService extends CrudService<CountryDto, Country> {
    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository,
                          CountryMapper countryMapper,
                          CountryValidator countryValidator) {
        super(countryRepository, countryMapper, countryValidator);
        this.countryRepository = countryRepository;
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
}
