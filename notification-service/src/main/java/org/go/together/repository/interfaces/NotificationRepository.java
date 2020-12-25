package org.go.together.repository.interfaces;

import org.go.together.base.CustomRepository;
import org.go.together.model.Notification;

import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends CustomRepository<Notification> {
    Optional<Notification> findByProducerId(UUID producerId);
}
