package org.go.together.notification.repository;

import org.go.together.model.Country;
import org.go.together.repository.CustomRepository;
import org.go.together.repository.sql.SqlOperator;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Repository
public class CountryRepository extends CustomRepository<Country> {
    @Transactional
    public Optional<Country> findByName(String name) {
        return createQuery()
                .where(createWhere().condition("name", SqlOperator.EQUAL, name.split("\\(")[0].trim().toUpperCase()))
                .fetchOne();
    }

    @Transactional
    public Collection<Country> findCountriesLike(String countryName) {
        return createQuery()
                .where(createWhere().condition("name", SqlOperator.LIKE, countryName.toUpperCase()))
                .fetchAll();
    }
}