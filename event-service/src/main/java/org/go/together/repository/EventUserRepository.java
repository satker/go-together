package org.go.together.repository;

import org.go.together.logic.repository.CustomRepository;
import org.go.together.logic.repository.utils.sql.SqlOperator;
import org.go.together.model.EventUser;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EventUserRepository extends CustomRepository<EventUser> {
    @Transactional
    public Collection<EventUser> findEventUserByEventId(UUID eventId) {
        return createQuery().where(createWhere().condition("eventId", SqlOperator.EQUAL, eventId)).fetchAll();
    }

    @Transactional
    public Optional<EventUser> findEventUserByUserIdAndEventId(UUID userId, UUID eventId) {
        return createQuery().where(createWhere().condition("userId", SqlOperator.EQUAL, userId).and()
                .condition("eventId", SqlOperator.EQUAL, eventId)).fetchOne();
    }
}
