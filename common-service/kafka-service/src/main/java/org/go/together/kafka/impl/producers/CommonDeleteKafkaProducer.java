package org.go.together.kafka.impl.producers;

import org.go.together.dto.Dto;
import org.go.together.kafka.producers.crud.DeleteKafkaProducer;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;


public abstract class CommonDeleteKafkaProducer<D extends Dto> implements DeleteKafkaProducer<D> {
    private final KafkaTemplate<UUID, UUID> kafkaTemplate;

    private CommonDeleteKafkaProducer(KafkaTemplate<UUID, UUID> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public static <D extends Dto> DeleteKafkaProducer<D> create(KafkaTemplate<UUID, UUID> kafkaTemplate,
                                                                String consumerId) {
        return new CommonDeleteKafkaProducer<>(kafkaTemplate) {
            @Override
            public String getTopicId() {
                return consumerId;
            }
        };
    }

    public KafkaTemplate<UUID, UUID> getKafkaTemplate() {
        return kafkaTemplate;
    }
}
