package org.go.together.kafka.interfaces.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.ValidationMessageDto;
import org.go.together.dto.form.FormDto;

import java.util.UUID;

public interface KafkaConsumer<D extends Dto> {
    IdDto handleCreate(ConsumerRecord<UUID, D> message);

    IdDto handleUpdate(ConsumerRecord<UUID, D> message);

    void handleDelete(ConsumerRecord<UUID, UUID> message);

    D handleRead(ConsumerRecord<UUID, UUID> message);

    ValidationMessageDto handleValidate(ConsumerRecord<UUID, D> message);

    ResponseDto<Object> handleFind(ConsumerRecord<UUID, FormDto> message);
}
