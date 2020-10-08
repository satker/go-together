package org.go.together.repository.interfaces;

import org.go.together.model.EventLike;
import org.go.together.repository.CustomRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface EventLikeRepository extends CustomRepository<EventLike> {
    Optional<EventLike> findByEventId(UUID eventId);

    Collection<EventLike> findByUserId(UUID userId);
}
