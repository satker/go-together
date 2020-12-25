package org.go.together.repository.interfaces;

import org.go.together.base.CustomRepository;
import org.go.together.model.Place;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface PlaceRepository extends CustomRepository<Place> {
    Collection<Place> findLocationByName(String location, int start, int pageSize);
    Collection<Place> findLocationByNameAndByCountryId(String location, Collection<UUID> countryIds,
                                                       int start, int pageSize);
    Optional<Place> findLocationByNameAndStateAndByCountryId(String location, String state, UUID countryId);
    Optional<Place> findByLocationId(UUID locationId);
}
