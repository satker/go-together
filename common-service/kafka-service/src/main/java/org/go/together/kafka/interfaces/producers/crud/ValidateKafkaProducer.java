package org.go.together.kafka.interfaces.producers.crud;

import org.go.together.dto.Dto;
import org.go.together.dto.ValidationMessageDto;
import org.go.together.kafka.interfaces.TopicKafkaPostfix;
import org.go.together.kafka.interfaces.producers.ReplyKafkaProducer;

import java.util.UUID;

public interface ValidateKafkaProducer<D extends Dto> extends ReplyKafkaProducer<D, ValidationMessageDto> {
    default ValidationMessageDto validate(UUID requestId, D dto) {
        String messageTopic = getTopicId() + TopicKafkaPostfix.VALIDATE.getDescription();
        return sendWithReply(messageTopic, requestId, dto);
    }
}
