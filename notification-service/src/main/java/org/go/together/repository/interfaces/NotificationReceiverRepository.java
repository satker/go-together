package org.go.together.repository.interfaces;

import org.go.together.base.CustomRepository;
import org.go.together.model.NotificationReceiver;

import java.util.Collection;
import java.util.UUID;

public interface NotificationReceiverRepository extends CustomRepository<NotificationReceiver> {
    Collection<NotificationReceiver> findByNotificationId(UUID notificationId);
    Collection<NotificationReceiver> findByProducerId(UUID producerId);
}
