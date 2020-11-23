package org.go.together.kafka.impl.producers;

import org.go.together.dto.Dto;
import org.go.together.kafka.interfaces.producers.crud.ReadKafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.util.UUID;

public abstract class CommonReadKafkaProducer<D extends Dto> implements ReadKafkaProducer<D> {
    private ReplyingKafkaTemplate<UUID, UUID, D> kafkaTemplate;

    @Value("${kafka.groupId}")
    private String groupId;

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public ReplyingKafkaTemplate<UUID, UUID, D> getReplyingKafkaTemplate() {
        return this.kafkaTemplate;
    }

    public void setReadReplyingKafkaTemplate(ReplyingKafkaTemplate<UUID, UUID, D> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}
