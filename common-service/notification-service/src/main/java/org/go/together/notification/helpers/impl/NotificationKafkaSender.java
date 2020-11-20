package org.go.together.notification.helpers.impl;

import org.go.together.kafka.NotificationEvent;
import org.go.together.notification.helpers.interfaces.KafkaSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NotificationKafkaSender implements KafkaSender {
    private final KafkaTemplate<UUID, NotificationEvent> kafkaTemplate;
    @Value("${kafka.notificationTopicId}")
    private String notificationTopicId;

    public NotificationKafkaSender(KafkaTemplate<UUID, NotificationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(UUID id, NotificationEvent notificationEvent) {
        kafkaTemplate.send(notificationTopicId, id, notificationEvent);
    }
}
