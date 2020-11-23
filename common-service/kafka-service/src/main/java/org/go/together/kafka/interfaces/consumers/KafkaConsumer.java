package org.go.together.kafka.interfaces.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.springframework.messaging.Message;

import java.util.UUID;

public interface KafkaConsumer<D extends Dto> {
    IdDto handleCreate(ConsumerRecord<UUID, D> message);

    IdDto handleUpdate(ConsumerRecord<UUID, D> message);

    void handleDelete(ConsumerRecord<UUID, UUID> message);

    Message<D> handleRead(ConsumerRecord<UUID, UUID> message);

    String handleValidate(ConsumerRecord<UUID, D> message);
}
