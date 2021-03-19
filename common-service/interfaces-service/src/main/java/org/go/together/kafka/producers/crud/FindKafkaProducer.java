package org.go.together.kafka.producers.crud;

import org.go.together.dto.Dto;
import org.go.together.dto.FormDto;
import org.go.together.dto.ResponseDto;
import org.go.together.interfaces.TopicKafkaPostfix;
import org.go.together.kafka.producers.ReplyKafkaProducer;

public interface FindKafkaProducer<D extends Dto> extends ReplyKafkaProducer<FormDto, ResponseDto<Object>> {
    default ResponseDto<Object> find(FormDto dto) {
        String messageTopic = getTopicId() + TopicKafkaPostfix.FIND;
        return sendWithReply(messageTopic, dto);
    }
}
