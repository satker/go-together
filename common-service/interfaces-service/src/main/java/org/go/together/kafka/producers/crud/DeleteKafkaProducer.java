package org.go.together.kafka.producers.crud;

import org.go.together.dto.Dto;
import org.go.together.enums.TopicKafkaPostfix;
import org.go.together.kafka.producers.KafkaProducer;

import java.util.UUID;

public interface DeleteKafkaProducer<D extends Dto> extends KafkaProducer<UUID> {
    default void delete(UUID requestId, UUID dtoId) {
        String messageTopic = getTopicId() + TopicKafkaPostfix.DELETE.getDescription();
        send(messageTopic, requestId, dtoId);
    }
}
