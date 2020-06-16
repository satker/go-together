package org.go.together.repository;

import org.go.together.CustomRepository;
import org.go.together.model.Location;
import org.go.together.sql.SqlOperator;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.UUID;

@Repository
public class LocationRepository extends CustomRepository<Location> {
    @Transactional
    public Collection<Location> findLocationByName(String location, int start, int pageSize) {
        return createQuery()
                .where(createWhere().condition("name", SqlOperator.LIKE, location.toLowerCase()))
                .fetchWithPageable(start, pageSize);
    }

    @Transactional
    public Collection<Location> findLocationByNameAndByCountryId(String location, Collection<UUID> countryIds,
                                                                 int start, int pageSize) {
        return createQuery()
                .where(createWhere().condition("name", SqlOperator.LIKE, location.toLowerCase())
                        .and().condition("country_id", SqlOperator.IN, countryIds))
                .fetchWithPageable(start, pageSize);
    }
}

