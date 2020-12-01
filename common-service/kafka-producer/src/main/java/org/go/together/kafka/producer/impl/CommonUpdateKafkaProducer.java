package org.go.together.kafka.producer.impl;

import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.go.together.kafka.producers.crud.UpdateKafkaProducer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.util.UUID;

public abstract class CommonUpdateKafkaProducer<D extends Dto> implements UpdateKafkaProducer<D> {
    private final ReplyingKafkaTemplate<UUID, D, IdDto> kafkaTemplate;
    private final String groupId;

    private CommonUpdateKafkaProducer(ReplyingKafkaTemplate<UUID, D, IdDto> kafkaTemplate,
                                      String groupId) {
        this.kafkaTemplate = kafkaTemplate;
        this.groupId = groupId;
    }

    public static <D extends Dto> UpdateKafkaProducer<D> create(ReplyingKafkaTemplate<UUID, D, IdDto> kafkaTemplate,
                                                                String groupId,
                                                                String consumerId) {
        return new CommonUpdateKafkaProducer<>(kafkaTemplate, groupId) {
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

    public ReplyingKafkaTemplate<UUID, D, IdDto> getReplyingKafkaTemplate() {
        return this.kafkaTemplate;
    }
}
