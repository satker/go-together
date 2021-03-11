package org.go.together.kafka.producer.impl;

import brave.Tracer;
import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.go.together.kafka.producers.crud.UpdateKafkaProducer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

public abstract class CommonUpdateKafkaProducer<D extends Dto> implements UpdateKafkaProducer<D> {
    private final ReplyingKafkaTemplate<Long, D, IdDto> kafkaTemplate;
    private final String groupId;

    private CommonUpdateKafkaProducer(ReplyingKafkaTemplate<Long, D, IdDto> kafkaTemplate,
                                      String groupId) {
        this.kafkaTemplate = kafkaTemplate;
        this.groupId = groupId;
    }

    public static <D extends Dto> UpdateKafkaProducer<D> create(ReplyingKafkaTemplate<Long, D, IdDto> kafkaTemplate,
                                                                String groupId,
                                                                String consumerId,
                                                                Tracer tracer) {
        return new CommonUpdateKafkaProducer<>(kafkaTemplate, groupId) {
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

    public ReplyingKafkaTemplate<Long, D, IdDto> getReplyingKafkaTemplate() {
        return this.kafkaTemplate;
    }
}
