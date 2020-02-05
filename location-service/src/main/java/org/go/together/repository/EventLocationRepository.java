package org.go.together.repository;

import org.go.together.logic.repository.CustomRepository;
import org.go.together.logic.repository.utils.sql.SqlOperator;
import org.go.together.model.EventLocation;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.UUID;

@Repository
public class EventLocationRepository extends CustomRepository<EventLocation> {
    @Transactional
    public Collection<EventLocation> findByEventId(UUID id) {
        return createQuery().where(createWhere().condition("eventId", SqlOperator.EQUAL, id)).fetchAll();
    }
}
