package org.go.together.kafka.producers.crud;

import org.go.together.dto.Dto;
import org.go.together.interfaces.TopicKafkaPostfix;
import org.go.together.kafka.producers.ReplyKafkaProducer;

import java.util.UUID;

public interface ReadKafkaProducer<D extends Dto> extends ReplyKafkaProducer<UUID, D> {
    default D read(UUID dtoId) {
        String messageTopic = getTopicId() + TopicKafkaPostfix.READ;
        return sendWithReply(messageTopic, dtoId);
    }
}
