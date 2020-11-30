package org.go.together.kafka.impl.producers;

import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.go.together.kafka.producers.crud.CreateKafkaProducer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.util.UUID;

public abstract class CommonCreateKafkaProducer<D extends Dto> implements CreateKafkaProducer<D> {
    private final ReplyingKafkaTemplate<UUID, D, IdDto> kafkaTemplate;
    private final String groupId;

    public static <D extends Dto> CreateKafkaProducer<D> create(ReplyingKafkaTemplate<UUID, D, IdDto> kafkaTemplate,
                                                                String groupId,
                                                                String consumerId) {
        return new CommonCreateKafkaProducer<>(kafkaTemplate, groupId) {
            @Override
            public String getTopicId() {
                return consumerId;
            }
        };
    }

    public CommonCreateKafkaProducer(ReplyingKafkaTemplate<UUID, D, IdDto> kafkaTemplate,
                                     String groupId) {
        this.kafkaTemplate = kafkaTemplate;
        this.groupId = groupId;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public ReplyingKafkaTemplate<UUID, D, IdDto> getReplyingKafkaTemplate() {
        return this.kafkaTemplate;
    }
}
