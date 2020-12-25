package org.go.together.repository.interfaces;

import org.go.together.base.CustomRepository;
import org.go.together.model.NotificationMessage;

import java.util.Collection;
import java.util.UUID;

public interface NotificationMessageRepository extends CustomRepository<NotificationMessage> {
    Collection<NotificationMessage> findByNotificationId(UUID notificationId);
}
