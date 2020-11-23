package org.go.together.kafka.interfaces.producers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.go.together.exceptions.ApplicationException;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface ReplyKafkaProducer<T, R> {
    String KAFKA_REPLY_ID = "_reply_";

    String CORRELATION_PREFIX = "_correlationId";

    String getGroupId();

    ReplyingKafkaTemplate<UUID, T, R> getReplyingKafkaTemplate();

    String getTopicId();

    default R sendWithReply(String targetTopic, UUID id, T object) {
        ProducerRecord<UUID, T> record = new ProducerRecord<>(targetTopic, null, id, object);
        String replyTopicId = targetTopic + KAFKA_REPLY_ID + getGroupId();
        record.headers().add(KafkaHeaders.REPLY_TOPIC, replyTopicId.getBytes());
        String correlationId = getGroupId() + CORRELATION_PREFIX;
        record.headers().add(KafkaHeaders.CORRELATION_ID, correlationId.getBytes());
        RequestReplyFuture<UUID, T, R> result = getReplyingKafkaTemplate().sendAndReceive(record);
        try {
            ConsumerRecord<UUID, R> futureGet = result.get(1500, TimeUnit.MILLISECONDS);
            return futureGet.value();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new ApplicationException(id.toString() + e.getMessage());
        }
    }
}
