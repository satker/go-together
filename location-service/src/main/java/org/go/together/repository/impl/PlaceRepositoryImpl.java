package org.go.together.repository.impl;

import org.go.together.model.Place;
import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.interfaces.PlaceRepository;
import org.go.together.repository.sql.SqlOperator;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PlaceRepositoryImpl extends CustomRepositoryImpl<Place> implements PlaceRepository {
    @Override
    public Collection<Place> findLocationByName(String location, int start, int pageSize) {
        return createQuery()
                .where(createWhere().condition("name", SqlOperator.LIKE_LOWER_CASE, location)).build()
                .fetchWithPageable(start, pageSize);
    }

    @Override
    public Collection<Place> findLocationByNameAndByCountryId(String location, Collection<UUID> countryIds,
                                                              int start, int pageSize) {
        return createQuery()
                .where(createWhere().condition("name", SqlOperator.LIKE_LOWER_CASE, location)
                        .and().condition("country.id", SqlOperator.IN, countryIds)).build()
                .fetchWithPageable(start, pageSize);
    }

    @Override
    public Optional<Place> findLocationByNameAndStateAndByCountryId(String location,
                                                                    String state,
                                                                    UUID countryId) {
        return createQuery()
                .where(createWhere().condition("name", SqlOperator.LIKE_LOWER_CASE, location.toLowerCase())
                        .and().condition("state", SqlOperator.LIKE_LOWER_CASE, state.toLowerCase())
                        .and().condition("country.id", SqlOperator.EQUAL, countryId)).build()
                .fetchOne();
    }

    @Override
    public Optional<Place> findByLocationId(UUID locationId) {
        return createQuery()
                .where(createWhere().condition("locations.id", SqlOperator.EQUAL, locationId)).build()
                .fetchOne();
    }
}

