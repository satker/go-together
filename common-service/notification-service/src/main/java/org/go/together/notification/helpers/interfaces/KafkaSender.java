package org.go.together.notification.helpers.interfaces;

import org.go.together.kafka.NotificationEvent;

import java.util.UUID;

public interface KafkaSender {
    void send(UUID id, NotificationEvent notificationEvent);
}
