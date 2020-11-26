package org.go.together.kafka.impl.producers;

import org.go.together.dto.Dto;
import org.go.together.dto.ValidationMessageDto;
import org.go.together.kafka.producers.crud.ValidateKafkaProducer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.util.UUID;

public abstract class CommonValidateKafkaProducer<D extends Dto> implements ValidateKafkaProducer<D> {
    private final ReplyingKafkaTemplate<UUID, D, ValidationMessageDto> kafkaTemplate;
    private final String groupId;

    protected CommonValidateKafkaProducer(ReplyingKafkaTemplate<UUID, D, ValidationMessageDto> kafkaTemplate, String groupId) {
        this.kafkaTemplate = kafkaTemplate;
        this.groupId = groupId;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public ReplyingKafkaTemplate<UUID, D, ValidationMessageDto> getReplyingKafkaTemplate() {
        return this.kafkaTemplate;
    }
}
