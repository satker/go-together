package org.go.together.repository.interfaces;

import org.go.together.model.EventUser;
import org.go.together.repository.CustomRepository;

import java.util.Optional;
import java.util.UUID;

public interface EventUserRepository extends CustomRepository<EventUser> {
    Optional<EventUser> findEventUserByUserIdAndEventId(UUID userId, UUID eventId);
}
