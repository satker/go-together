package org.go.together.kafka.interfaces.producers.crud;

import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.go.together.kafka.interfaces.TopicKafkaPostfix;
import org.go.together.kafka.interfaces.producers.ReplyKafkaProducer;

import java.util.UUID;

public interface UpdateKafkaProducer<D extends Dto> extends ReplyKafkaProducer<D, IdDto> {
    default IdDto update(UUID requestId, D dto) {
        String messageTopic = getTopicId() + TopicKafkaPostfix.UPDATE.getDescription();
        return sendWithReply(messageTopic, requestId, dto);
    }
}
