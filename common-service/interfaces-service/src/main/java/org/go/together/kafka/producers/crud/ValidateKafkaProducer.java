package org.go.together.kafka.producers.crud;

import org.go.together.dto.Dto;
import org.go.together.dto.ValidationMessageDto;
import org.go.together.enums.TopicKafkaPostfix;
import org.go.together.kafka.producers.ReplyKafkaProducer;

public interface ValidateKafkaProducer<D extends Dto> extends ReplyKafkaProducer<D, ValidationMessageDto> {
    default ValidationMessageDto validate(D dto) {
        String messageTopic = getTopicId() + TopicKafkaPostfix.VALIDATE;
        return sendWithReply(messageTopic, dto);
    }
}
