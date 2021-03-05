package org.go.together.kafka.producer.impl;

import brave.Tracer;
import org.go.together.dto.Dto;
import org.go.together.dto.FormDto;
import org.go.together.dto.ResponseDto;
import org.go.together.kafka.producers.crud.FindKafkaProducer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

public abstract class CommonFindKafkaProducer<D extends Dto> implements FindKafkaProducer<D> {
    private final ReplyingKafkaTemplate<Long, FormDto, ResponseDto<Object>> kafkaTemplate;
    private final String groupId;

    protected CommonFindKafkaProducer(ReplyingKafkaTemplate<Long, FormDto, ResponseDto<Object>> kafkaTemplate,
                                      String groupId) {
        this.kafkaTemplate = kafkaTemplate;
        this.groupId = groupId;
    }

    public static <D extends Dto> FindKafkaProducer<D> create(ReplyingKafkaTemplate<Long, FormDto, ResponseDto<Object>> kafkaTemplate,
                                                              String consumerId,
                                                              String groupId,
                                                              Tracer tracer) {
        return new CommonFindKafkaProducer<>(kafkaTemplate, groupId) {
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
    public ReplyingKafkaTemplate<Long, FormDto, ResponseDto<Object>> getReplyingKafkaTemplate() {
        return this.kafkaTemplate;
    }
}
