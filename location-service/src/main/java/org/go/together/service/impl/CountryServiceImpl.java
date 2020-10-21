package org.go.together.service.impl;

import org.go.together.base.impl.CommonCrudService;
import org.go.together.dto.CountryDto;
import org.go.together.model.Country;
import org.go.together.repository.interfaces.CountryRepository;
import org.go.together.service.interfaces.CountryService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CountryServiceImpl extends CommonCrudService<CountryDto, Country> implements CountryService {
    public Collection<Country> findCountriesLike(String countryName) {
        return ((CountryRepository) repository).findCountriesLike(countryName);
    }

    @Override
    public String getServiceName() {
        return "country";
    }
}
