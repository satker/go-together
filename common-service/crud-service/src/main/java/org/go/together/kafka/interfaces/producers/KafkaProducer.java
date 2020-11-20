package org.go.together.kafka.interfaces.producers;

import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

public interface KafkaProducer<T> {
    KafkaTemplate<UUID, T> getKafkaTemplate();

    void setKafkaTemplate(KafkaTemplate<UUID, T> kafkaTemplate);

    String getTopicId();

    default void send(String notificationTopic, UUID id, T object) {
        getKafkaTemplate().send(notificationTopic, id, object);
    }
}
