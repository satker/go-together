package org.go.together.kafka.impl.producers;

import org.go.together.dto.Dto;
import org.go.together.kafka.producers.crud.ReadKafkaProducer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.util.UUID;

public abstract class CommonReadKafkaProducer<D extends Dto> implements ReadKafkaProducer<D> {
    private final ReplyingKafkaTemplate<UUID, UUID, D> kafkaTemplate;
    private final String groupId;

    private CommonReadKafkaProducer(ReplyingKafkaTemplate<UUID, UUID, D> kafkaTemplate,
                                    String groupId) {
        this.kafkaTemplate = kafkaTemplate;
        this.groupId = groupId;
    }

    public static <D extends Dto> ReadKafkaProducer<D> create(ReplyingKafkaTemplate<UUID, UUID, D> kafkaTemplate,
                                                              String groupId,
                                                              String consumerId) {
        return new CommonReadKafkaProducer<>(kafkaTemplate, groupId) {

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
    public ReplyingKafkaTemplate<UUID, UUID, D> getReplyingKafkaTemplate() {
        return this.kafkaTemplate;
    }
}
