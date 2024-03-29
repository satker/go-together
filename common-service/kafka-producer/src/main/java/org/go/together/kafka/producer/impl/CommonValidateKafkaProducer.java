package org.go.together.kafka.producer.impl;

import brave.Tracer;
import org.go.together.dto.Dto;
import org.go.together.dto.ValidationMessageDto;
import org.go.together.kafka.producers.crud.ValidateKafkaProducer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

public abstract class CommonValidateKafkaProducer<D extends Dto> implements ValidateKafkaProducer<D> {
    private final ReplyingKafkaTemplate<Long, D, ValidationMessageDto> kafkaTemplate;
    private final String groupId;

    private CommonValidateKafkaProducer(ReplyingKafkaTemplate<Long, D, ValidationMessageDto> kafkaTemplate, String groupId) {
        this.kafkaTemplate = kafkaTemplate;
        this.groupId = groupId;
    }

    public static <D extends Dto> ValidateKafkaProducer<D> create(ReplyingKafkaTemplate<Long, D, ValidationMessageDto> kafkaTemplate,
                                                                  String groupId,
                                                                  String consumerId,
                                                                  Tracer tracer) {
        return new CommonValidateKafkaProducer<>(kafkaTemplate, groupId) {

            @Override
            public String getTopicId() {
                return consumerId;
            }

            @Override
            public Tracer getTracer() {
                return tracer;
            }
        };
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public ReplyingKafkaTemplate<Long, D, ValidationMessageDto> getReplyingKafkaTemplate() {
        return this.kafkaTemplate;
    }
}
