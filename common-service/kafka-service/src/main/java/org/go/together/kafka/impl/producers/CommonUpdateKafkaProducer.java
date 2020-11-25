package org.go.together.kafka.impl.producers;

import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.go.together.kafka.interfaces.producers.crud.UpdateKafkaProducer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.util.UUID;

public abstract class CommonUpdateKafkaProducer<D extends Dto> implements UpdateKafkaProducer<D> {
    private final ReplyingKafkaTemplate<UUID, D, IdDto> kafkaTemplate;
    private final String groupId;

    protected CommonUpdateKafkaProducer(ReplyingKafkaTemplate<UUID, D, IdDto> kafkaTemplate,
                                        String groupId) {
        this.kafkaTemplate = kafkaTemplate;
        this.groupId = groupId;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    public ReplyingKafkaTemplate<UUID, D, IdDto> getReplyingKafkaTemplate() {
        return this.kafkaTemplate;
    }
}
