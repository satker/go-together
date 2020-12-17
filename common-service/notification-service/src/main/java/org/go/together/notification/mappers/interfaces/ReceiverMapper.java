package org.go.together.notification.mappers.interfaces;

import org.go.together.kafka.NotificationEvent;

import java.util.UUID;

public interface ReceiverMapper {
    NotificationEvent getNotificationEvent(UUID producerId, UUID receiverId);
}
