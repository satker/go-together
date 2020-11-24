package org.go.together.kafka.impl.producers;

import org.go.together.dto.Dto;
import org.go.together.kafka.interfaces.producers.crud.ValidateKafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.util.UUID;

public abstract class CommonValidateKafkaProducer<D extends Dto> implements ValidateKafkaProducer<D> {
    private ReplyingKafkaTemplate<UUID, D, String> kafkaTemplate;

    @Value("${kafka.groupId}")
    private String groupId;

    @Override
    public String getGroupId() {
        return groupId;
    }

    public ReplyingKafkaTemplate<UUID, D, String> getReplyingKafkaTemplate() {
        return this.kafkaTemplate;
    }

    public void setValidateReplyingKafkaTemplate(ReplyingKafkaTemplate<UUID, D, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}
