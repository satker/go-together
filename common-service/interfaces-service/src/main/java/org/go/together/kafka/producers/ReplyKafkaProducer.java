package org.go.together.kafka.producers;

import brave.Tracer;
import brave.propagation.TraceContext;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.go.together.exceptions.ApplicationException;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.go.together.enums.ExtraKafkaHeaders.SPAN_ID;
import static org.go.together.enums.ExtraKafkaHeaders.TRACE_ID;

public interface ReplyKafkaProducer<T, R> {
    String KAFKA_REPLY_ID = "_reply_";

    String CORRELATION_PREFIX = "_correlationId";

    String getGroupId();

    ReplyingKafkaTemplate<UUID, T, R> getReplyingKafkaTemplate();

    String getTopicId();

    Tracer getTracer();

    default R sendWithReply(String targetTopic, UUID requestId, T object) {
        ProducerRecord<UUID, T> record = getRecordToSend(targetTopic, requestId, object);
        RequestReplyFuture<UUID, T, R> result = getReplyingKafkaTemplate().sendAndReceive(record);
        try {
            return result.get(1500, TimeUnit.MILLISECONDS).value();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new ApplicationException("Kafka exception. Cannot send request to " + targetTopic, requestId);
        }
    }

    private ProducerRecord<UUID, T> getRecordToSend(String targetTopic, UUID requestId, T object) {
        ProducerRecord<UUID, T> record = new ProducerRecord<>(targetTopic, null, requestId, object);
        String replyTopicId = targetTopic + KAFKA_REPLY_ID + getGroupId();
        record.headers().add(KafkaHeaders.REPLY_TOPIC, replyTopicId.getBytes());
        String correlationId = getGroupId() + CORRELATION_PREFIX;
        record.headers().add(KafkaHeaders.CORRELATION_ID, correlationId.getBytes());
        TraceContext traceContext = getTracer().currentSpan().context();
        record.headers().add(TRACE_ID.getDescription(), String.valueOf(traceContext.traceId()).getBytes());
        record.headers().add(SPAN_ID.getDescription(), String.valueOf(traceContext.spanId()).getBytes());
        return record;
    }
}
