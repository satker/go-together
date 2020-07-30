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
    public Collection<Location> findByEventId(UUID id) {
        return createQuery().where(createWhere().condition("eventId", SqlOperator.EQUAL, id)).fetchAll();
    }

    @Transactional
    public Number getCountPlaceIdRows(UUID placeId) {
        return createQuery().where(createWhere().condition("place", SqlOperator.EQUAL, placeId)).getCountRows();
    }
}
