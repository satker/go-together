package org.go.together.kafka.interfaces.producers.crud;

import org.go.together.dto.Dto;
import org.go.together.kafka.interfaces.TopicKafkaPostfix;
import org.go.together.kafka.interfaces.producers.ReplyKafkaProducer;

import java.util.UUID;

public interface ValidateKafkaProducer<D extends Dto> extends ReplyKafkaProducer<D, String> {
    default String validate(UUID id, D dto) {
        String messageTopic = getTopicId() + TopicKafkaPostfix.VALIDATE.getDescription();
        return sendWithReply(messageTopic, id, dto);
    }
}
