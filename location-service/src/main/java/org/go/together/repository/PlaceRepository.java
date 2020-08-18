package org.go.together.repository;

import org.go.together.model.Place;
import org.go.together.repository.sql.SqlOperator;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PlaceRepository extends CustomRepository<Place> {
    @Transactional
    public Collection<Place> findLocationByName(String location, int start, int pageSize) {
        return createQuery()
                .where(createWhere().condition("name", SqlOperator.LIKE_LOWER_CASE, location))
                .fetchWithPageable(start, pageSize);
    }

    @Transactional
    public Collection<Place> findLocationByNameAndByCountryId(String location, Collection<UUID> countryIds,
                                                              int start, int pageSize) {
        return createQuery()
                .where(createWhere().condition("name", SqlOperator.LIKE_LOWER_CASE, location)
                        .and().condition("country.id", SqlOperator.IN, countryIds))
                .fetchWithPageable(start, pageSize);
    }

    @Transactional
    public Optional<Place> findLocationByNameAndStateAndByCountryId(String location,
                                                                    String state,
                                                                    UUID countryId) {
        return createQuery()
                .where(createWhere().condition("name", SqlOperator.LIKE_LOWER_CASE, location.toLowerCase())
                        .and().condition("state", SqlOperator.LIKE_LOWER_CASE, state.toLowerCase())
                        .and().condition("country.id", SqlOperator.EQUAL, countryId))
                .fetchOne();
    }

    @Transactional
    public Optional<Place> findByLocationId(UUID locationId) {
        return createQuery()
                .where(createWhere().condition("locations.id", SqlOperator.EQUAL, locationId))
                .fetchOne();
    }
}

