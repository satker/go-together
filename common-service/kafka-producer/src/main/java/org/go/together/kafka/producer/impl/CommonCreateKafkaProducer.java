package org.go.together.kafka.producer.impl;

import brave.Tracer;
import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.go.together.kafka.producers.crud.CreateKafkaProducer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

public abstract class CommonCreateKafkaProducer<D extends Dto> implements CreateKafkaProducer<D> {
    private final ReplyingKafkaTemplate<Long, D, IdDto> kafkaTemplate;
    private final String groupId;

    public CommonCreateKafkaProducer(ReplyingKafkaTemplate<Long, D, IdDto> kafkaTemplate,
                                     String groupId) {
        this.kafkaTemplate = kafkaTemplate;
        this.groupId = groupId;
    }

    public static <D extends Dto> CreateKafkaProducer<D> create(ReplyingKafkaTemplate<Long, D, IdDto> kafkaTemplate,
                                                                String groupId,
                                                                String consumerId,
                                                                Tracer tracer) {
        return new CommonCreateKafkaProducer<>(kafkaTemplate, groupId) {
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
    public ReplyingKafkaTemplate<Long, D, IdDto> getReplyingKafkaTemplate() {
        return this.kafkaTemplate;
    }
}
