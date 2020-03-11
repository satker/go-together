package org.go.together.repository;

import org.go.together.logic.repository.CustomRepository;
import org.go.together.logic.repository.utils.sql.SqlOperator;
import org.go.together.model.EventUser;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.UUID;

@Repository
public class EventUserRepository extends CustomRepository<EventUser> {
    @Transactional
    public Collection<EventUser> findEventUserByEventId(UUID eventId) {
        return createQuery().where(createWhere().condition("eventId", SqlOperator.EQUAL, eventId)).fetchAll();
    }
}
