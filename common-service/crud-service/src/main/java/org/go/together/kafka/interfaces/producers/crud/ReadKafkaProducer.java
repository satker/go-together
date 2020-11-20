package org.go.together.kafka.interfaces.producers.crud;

import org.go.together.dto.Dto;
import org.go.together.kafka.interfaces.TopicKafkaPostfix;
import org.go.together.kafka.interfaces.producers.ReplyKafkaProducer;

import java.util.UUID;

public interface ReadKafkaProducer<D extends Dto> extends ReplyKafkaProducer<UUID, D> {
    default D read(UUID id, UUID dtoId) {
        String messageTopic = getTopicId() + TopicKafkaPostfix.READ.getDescription();
        return sendWithReply(messageTopic, id, dtoId);
    }
}
