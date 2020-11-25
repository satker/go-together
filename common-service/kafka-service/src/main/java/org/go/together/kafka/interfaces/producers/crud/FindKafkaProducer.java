package org.go.together.kafka.interfaces.producers.crud;

import org.go.together.dto.ResponseDto;
import org.go.together.dto.form.FormDto;
import org.go.together.kafka.interfaces.TopicKafkaPostfix;
import org.go.together.kafka.interfaces.producers.ReplyKafkaProducer;

import java.util.UUID;

public interface FindKafkaProducer extends ReplyKafkaProducer<FormDto, ResponseDto<Object>> {
    default ResponseDto<Object> validate(UUID requestId, FormDto dto) {
        String messageTopic = getTopicId() + TopicKafkaPostfix.FIND.getDescription();
        return sendWithReply(messageTopic, requestId, dto);
    }
}
