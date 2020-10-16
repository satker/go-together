package org.go.together.repository.interfaces;

import org.go.together.model.NotificationMessage;
import org.go.together.repository.CustomRepository;

import java.util.Collection;
import java.util.UUID;

public interface NotificationMessageRepository extends CustomRepository<NotificationMessage> {
    Collection<NotificationMessage> findByNotificationId(UUID notificationId);
}
