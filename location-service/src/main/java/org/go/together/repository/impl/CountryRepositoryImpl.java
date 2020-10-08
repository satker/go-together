package org.go.together.repository.impl;

import org.go.together.model.Country;
import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.interfaces.CountryRepository;
import org.go.together.repository.sql.SqlOperator;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class CountryRepositoryImpl extends CustomRepositoryImpl<Country> implements CountryRepository {
    @Override
    public Optional<Country> findByName(String name) {
        return createQuery()
                .where(createWhere().condition("name", SqlOperator.EQUAL, name.split("\\(")[0].trim().toUpperCase()))
                .fetchOne();
    }

    @Override
    public Collection<Country> findCountriesLike(String countryName) {
        return createQuery()
                .where(createWhere().condition("name", SqlOperator.LIKE, countryName.toUpperCase()))
                .fetchAll();
    }
}
