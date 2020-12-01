package org.go.together.kafka.producer.impl;

import org.go.together.dto.Dto;
import org.go.together.dto.FormDto;
import org.go.together.dto.ResponseDto;
import org.go.together.kafka.producers.crud.FindKafkaProducer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.util.UUID;

public abstract class CommonFindKafkaProducer<D extends Dto> implements FindKafkaProducer<D> {
    private final ReplyingKafkaTemplate<UUID, FormDto, ResponseDto<Object>> kafkaTemplate;
    private final String groupId;

    public static <D extends Dto> FindKafkaProducer<D> create(ReplyingKafkaTemplate<UUID, FormDto, ResponseDto<Object>> kafkaTemplate,
                                                              String consumerId,
                                                              String groupId) {
        return new CommonFindKafkaProducer<>(kafkaTemplate, groupId) {
            @Override
            public String getTopicId() {
                return consumerId;
            }
        };
    }

    protected CommonFindKafkaProducer(ReplyingKafkaTemplate<UUID, FormDto, ResponseDto<Object>> kafkaTemplate,
                                      String groupId) {
        this.kafkaTemplate = kafkaTemplate;
        this.groupId = groupId;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public ReplyingKafkaTemplate<UUID, FormDto, ResponseDto<Object>> getReplyingKafkaTemplate() {
        return this.kafkaTemplate;
    }
}