package org.go.together.repository.interfaces;

import org.go.together.model.Country;
import org.go.together.repository.CustomRepository;

import java.util.Collection;
import java.util.Optional;

public interface CountryRepository extends CustomRepository<Country> {
    Optional<Country> findByName(String name);
    Collection<Country> findCountriesLike(String countryName);
}
