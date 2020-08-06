package org.go.together.service;

import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.find.dto.FieldMapper;
import org.go.together.model.Country;
import org.go.together.notification.dto.CountryDto;
import org.go.together.notification.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public class CountryService extends CrudServiceImpl<CountryDto, Country> {
    public Collection<Country> findCountriesLike(String countryName) {
        return ((CountryRepository) repository).findCountriesLike(countryName);
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
