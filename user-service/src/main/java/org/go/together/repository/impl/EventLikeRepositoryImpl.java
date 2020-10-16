package org.go.together.repository.impl;

import org.go.together.model.EventLike;
import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.interfaces.EventLikeRepository;
import org.go.together.repository.sql.SqlOperator;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EventLikeRepositoryImpl extends CustomRepositoryImpl<EventLike> implements EventLikeRepository {
    @Override
    public Optional<EventLike> findByEventId(UUID eventId) {
        return createQuery().where(createWhere().condition("eventId", SqlOperator.EQUAL, eventId))
                .build()
                .fetchOne();
    }

    @Override
    public Collection<EventLike> findByUserId(UUID userId) {
        return createQuery().where(createWhere().condition("users.id", SqlOperator.EQUAL, userId))
                .build()
                .fetchAll();
    }
}
