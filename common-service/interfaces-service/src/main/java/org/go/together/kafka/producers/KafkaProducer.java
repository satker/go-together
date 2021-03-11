package org.go.together.kafka.producers;

import brave.Tracer;
import org.springframework.kafka.core.KafkaTemplate;

public interface KafkaProducer<T> {
    KafkaTemplate<Long, T> getKafkaTemplate();

    String getTopicId();

    Tracer getTracer();

    default void send(String notificationTopic, T object) {
        getKafkaTemplate().send(notificationTopic, getTracer().currentSpan().context().traceId(), object);
    }
}
