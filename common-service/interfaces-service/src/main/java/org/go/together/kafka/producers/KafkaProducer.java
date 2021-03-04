package org.go.together.kafka.producers;

import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

public interface KafkaProducer<T> {
    KafkaTemplate<UUID, T> getKafkaTemplate();

    String getTopicId();

    default void send(String notificationTopic, T object) {
        // TODO: fix to trace id
        getKafkaTemplate().send(notificationTopic, UUID.randomUUID(), object);
    }
}
