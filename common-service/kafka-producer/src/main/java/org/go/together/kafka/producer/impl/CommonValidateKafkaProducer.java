package org.go.together.kafka.producer.impl;

import org.go.together.dto.Dto;
import org.go.together.dto.ValidationMessageDto;
import org.go.together.kafka.producers.crud.ValidateKafkaProducer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.util.UUID;

public abstract class CommonValidateKafkaProducer<D extends Dto> implements ValidateKafkaProducer<D> {
    private final ReplyingKafkaTemplate<UUID, D, ValidationMessageDto> kafkaTemplate;
    private final String groupId;

    private CommonValidateKafkaProducer(ReplyingKafkaTemplate<UUID, D, ValidationMessageDto> kafkaTemplate, String groupId) {
        this.kafkaTemplate = kafkaTemplate;
        this.groupId = groupId;
    }

    public static <D extends Dto> ValidateKafkaProducer<D> create(ReplyingKafkaTemplate<UUID, D, ValidationMessageDto> kafkaTemplate,
                                                                  String groupId,
                                                                  String consumerId) {
        return new CommonValidateKafkaProducer<>(kafkaTemplate, groupId) {

            @Override
            public String getTopicId() {
                return consumerId;
            }
        };
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
