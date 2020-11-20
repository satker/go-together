package org.go.together.kafka.impl.producers;

import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.go.together.kafka.interfaces.producers.crud.UpdateKafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.util.UUID;

public abstract class CommonUpdateKafkaProducer<D extends Dto> implements UpdateKafkaProducer<D> {
    private ReplyingKafkaTemplate<UUID, D, IdDto> kafkaTemplate;

    @Value("${kafka.groupId}")
    private String groupId;

    @Override
    public String getCorrelationId() {
        return groupId;
    }

    public ReplyingKafkaTemplate<UUID, D, IdDto> getReplyingKafkaTemplate() {
        return this.kafkaTemplate;
    }

    public void setReplyingKafkaTemplate(ReplyingKafkaTemplate<UUID, D, IdDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}
