package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.dto.CountryDto;
import org.go.together.find.FindService;
import org.go.together.model.Country;

import java.util.Collection;

public interface CountryService extends CrudService<CountryDto>, FindService<Country> {
    Collection<Country> findCountriesLike(String countryName);
}
