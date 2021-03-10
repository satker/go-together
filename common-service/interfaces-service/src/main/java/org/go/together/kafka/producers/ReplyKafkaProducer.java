package org.go.together.kafka.producers;

import brave.Tracer;
import brave.propagation.TraceContext;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.go.together.exceptions.ApplicationException;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.go.together.utils.ByteUtils.longToBytes;

public interface ReplyKafkaProducer<T, R> {
    String KAFKA_REPLY_ID = "_reply_";

    String CORRELATION_PREFIX = "_correlationId";

    String PARENT_SPAN_ID = "parentSpanId";

    String getGroupId();

    ReplyingKafkaTemplate<Long, T, R> getReplyingKafkaTemplate();

    String getTopicId();

    Tracer getTracer();

    default R sendWithReply(String targetTopic, T object) {
        ProducerRecord<Long, T> record = getRecordToSend(targetTopic, object);
        RequestReplyFuture<Long, T, R> result = getReplyingKafkaTemplate().sendAndReceive(record);
        try {
            return result.get(1500, TimeUnit.MILLISECONDS).value();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new ApplicationException("Kafka exception. Cannot send request to " + targetTopic);
        }
    }

    private ProducerRecord<Long, T> getRecordToSend(String targetTopic, T object) {
        TraceContext traceContext = getTracer().currentSpan().context();
        ProducerRecord<Long, T> record = new ProducerRecord<>(targetTopic, null, traceContext.traceId(), object);
        String replyTopicId = targetTopic + KAFKA_REPLY_ID + getGroupId();
        String correlationId = getGroupId() + CORRELATION_PREFIX;
        record.headers().add(KafkaHeaders.REPLY_TOPIC, replyTopicId.getBytes());
        record.headers().add(KafkaHeaders.CORRELATION_ID, correlationId.getBytes());
        record.headers().add(PARENT_SPAN_ID, longToBytes(traceContext.spanId()));
        return record;
    }
}
