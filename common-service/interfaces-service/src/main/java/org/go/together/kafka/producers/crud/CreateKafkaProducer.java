package org.go.together.kafka.producers.crud;

import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.go.together.enums.TopicKafkaPostfix;
import org.go.together.kafka.producers.ReplyKafkaProducer;

import java.util.UUID;

public interface CreateKafkaProducer<D extends Dto> extends ReplyKafkaProducer<D, IdDto> {
    default IdDto create(UUID requestId, D dto) {
        String messageTopic = getTopicId() + TopicKafkaPostfix.CREATE;
        return sendWithReply(messageTopic, requestId, dto);
    }
}
