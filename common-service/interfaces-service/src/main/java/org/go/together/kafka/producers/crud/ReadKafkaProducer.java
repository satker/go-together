package org.go.together.kafka.producers.crud;

import org.go.together.dto.Dto;
import org.go.together.enums.TopicKafkaPostfix;
import org.go.together.kafka.producers.ReplyKafkaProducer;

import java.util.UUID;

public interface ReadKafkaProducer<D extends Dto> extends ReplyKafkaProducer<UUID, D> {
    default D read(UUID requestId, UUID dtoId) {
        String messageTopic = getTopicId() + TopicKafkaPostfix.READ.getDescription();
        return sendWithReply(messageTopic, requestId, dtoId);
    }
}
