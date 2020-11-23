package org.go.together.kafka.impl.producers;

import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.go.together.kafka.interfaces.producers.crud.CreateKafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.util.UUID;

public abstract class CommonCreateKafkaProducer<D extends Dto> implements CreateKafkaProducer<D> {
    private ReplyingKafkaTemplate<UUID, D, IdDto> kafkaTemplate;

    @Value("${kafka.groupId}")
    private String groupId;

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public ReplyingKafkaTemplate<UUID, D, IdDto> getReplyingKafkaTemplate() {
        return this.kafkaTemplate;
    }

    public void setCreateReplyingKafkaTemplate(ReplyingKafkaTemplate<UUID, D, IdDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}
