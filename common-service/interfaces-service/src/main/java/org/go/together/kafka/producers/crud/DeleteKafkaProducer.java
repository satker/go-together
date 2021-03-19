package org.go.together.kafka.producers.crud;

import org.go.together.dto.Dto;
import org.go.together.interfaces.TopicKafkaPostfix;
import org.go.together.kafka.producers.KafkaProducer;

import java.util.UUID;

public interface DeleteKafkaProducer<D extends Dto> extends KafkaProducer<UUID> {
    default void delete(UUID dtoId) {
        String messageTopic = getTopicId() + TopicKafkaPostfix.DELETE;
        send(messageTopic, dtoId);
    }
}
