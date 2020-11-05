package org.go.together.repository.impl;

import org.go.together.enums.SqlOperator;
import org.go.together.model.EventUser;
import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.interfaces.EventUserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class EventUserRepositoryImpl extends CustomRepositoryImpl<EventUser> implements EventUserRepository {
    @Override
    public Optional<EventUser> findEventUserByUserIdAndEventId(UUID userId, UUID eventId) {
        return createQuery().where(createWhere().condition("userId", SqlOperator.EQUAL, userId).and()
                .condition("eventId", SqlOperator.EQUAL, eventId)).build().fetchOne();
    }
}
