package org.go.together.repository.interfaces;

import org.go.together.base.CustomRepository;
import org.go.together.model.Country;

import java.util.Collection;
import java.util.Optional;

public interface CountryRepository extends CustomRepository<Country> {
    Optional<Country> findByName(String name);
    Collection<Country> findCountriesLike(String countryName);
}
