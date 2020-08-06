package org.go.together.repository;

import org.go.together.model.EventLike;
import org.go.together.repository.sql.SqlOperator;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EventLikeRepository extends CustomRepository<EventLike> {
    @Transactional
    public Optional<EventLike> findByEventId(UUID eventId) {
        return createQuery().where(createWhere().condition("eventId", SqlOperator.EQUAL, eventId))
                .fetchOne();
    }

    @Transactional
    public Collection<EventLike> findByUserId(UUID userId) {
        return createQuery().where(createWhere().condition("users.id", SqlOperator.EQUAL, userId))
                .fetchAll();
    }
}
