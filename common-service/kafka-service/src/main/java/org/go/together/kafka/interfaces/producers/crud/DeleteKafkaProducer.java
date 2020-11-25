package org.go.together.kafka.interfaces.producers.crud;

import org.go.together.dto.Dto;
import org.go.together.kafka.interfaces.TopicKafkaPostfix;
import org.go.together.kafka.interfaces.producers.KafkaProducer;

import java.util.UUID;

public interface DeleteKafkaProducer<D extends Dto> extends KafkaProducer<UUID> {
    default void delete(UUID requestId, UUID dtoId) {
        String messageTopic = getTopicId() + TopicKafkaPostfix.DELETE.getDescription();
        send(messageTopic, requestId, dtoId);
    }
}
