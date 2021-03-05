package org.go.together.kafka.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.go.together.dto.*;

import java.util.UUID;

public interface KafkaConsumer<D extends Dto> {
    IdDto handleCreate(ConsumerRecord<Long, D> message);

    IdDto handleUpdate(ConsumerRecord<Long, D> message);

    void handleDelete(ConsumerRecord<Long, UUID> message);

    D handleRead(ConsumerRecord<Long, UUID> message);

    ValidationMessageDto handleValidate(ConsumerRecord<Long, D> message);

    ResponseDto<Object> handleFind(ConsumerRecord<Long, FormDto> message);
}
