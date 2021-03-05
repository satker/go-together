package org.go.together.kafka.producer.impl;

import brave.Tracer;
import org.go.together.dto.Dto;
import org.go.together.kafka.producers.crud.DeleteKafkaProducer;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;


public abstract class CommonDeleteKafkaProducer<D extends Dto> implements DeleteKafkaProducer<D> {
    private final KafkaTemplate<Long, UUID> kafkaTemplate;

    private CommonDeleteKafkaProducer(KafkaTemplate<Long, UUID> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public static <D extends Dto> DeleteKafkaProducer<D> create(KafkaTemplate<Long, UUID> kafkaTemplate,
                                                                String consumerId,
                                                                Tracer tracer) {
        return new CommonDeleteKafkaProducer<>(kafkaTemplate) {
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

    public KafkaTemplate<Long, UUID> getKafkaTemplate() {
        return kafkaTemplate;
    }
}
