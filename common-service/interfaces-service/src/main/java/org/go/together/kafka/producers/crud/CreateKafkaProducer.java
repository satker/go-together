package org.go.together.kafka.producers.crud;

import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.go.together.enums.TopicKafkaPostfix;
import org.go.together.kafka.producers.ReplyKafkaProducer;

public interface CreateKafkaProducer<D extends Dto> extends ReplyKafkaProducer<D, IdDto> {
    default IdDto create(D dto) {
        String messageTopic = getTopicId() + TopicKafkaPostfix.CREATE;
        return sendWithReply(messageTopic, dto);
    }
}
