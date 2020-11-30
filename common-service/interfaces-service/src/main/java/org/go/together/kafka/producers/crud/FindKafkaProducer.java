package org.go.together.kafka.producers.crud;

import org.go.together.dto.Dto;
import org.go.together.dto.FormDto;
import org.go.together.dto.ResponseDto;
import org.go.together.enums.TopicKafkaPostfix;
import org.go.together.kafka.producers.ReplyKafkaProducer;

import java.util.UUID;

public interface FindKafkaProducer<D extends Dto> extends ReplyKafkaProducer<FormDto, ResponseDto<Object>> {
    default ResponseDto<Object> find(UUID requestId, FormDto dto) {
        String messageTopic = getTopicId() + TopicKafkaPostfix.FIND.getDescription();
        return sendWithReply(messageTopic, requestId, dto);
    }
}
