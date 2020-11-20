package org.go.together.kafka.interfaces.producers.crud;

import org.go.together.kafka.interfaces.TopicKafkaPostfix;
import org.go.together.kafka.interfaces.producers.KafkaProducer;

import java.util.UUID;

public interface DeleteKafkaProducer extends KafkaProducer<UUID> {
    default void delete(UUID id, UUID dtoId) {
        String messageTopic = getTopicId() + TopicKafkaPostfix.DELETE.getDescription();
        send(messageTopic, id, dtoId);
    }
}
