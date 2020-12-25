package org.go.together.consumers;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.go.together.base.CrudService;
import org.go.together.base.FindService;
import org.go.together.base.Validator;
import org.go.together.dto.*;
import org.go.together.kafka.consumer.impl.CommonCrudKafkaConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.go.together.enums.LocationServiceInfo.GROUP_LOCATION;
import static org.go.together.enums.TopicKafkaPostfix.*;
import static org.go.together.kafka.consumer.constants.ConsumerBeanConfigName.LISTENER_FACTORY;

@Component
@RequiredArgsConstructor
public class GroupLocationConsumer extends CommonCrudKafkaConsumer<GroupLocationDto> {
    private final CrudService<GroupLocationDto> service;
    private final Validator<GroupLocationDto> validator;
    private final FindService<GroupLocationDto> findService;

    @Override
    @KafkaListener(topics = GROUP_LOCATION + CREATE,
            containerFactory = GROUP_LOCATION + CHANGE + LISTENER_FACTORY)
    @SendTo
    public IdDto handleCreate(ConsumerRecord<UUID, GroupLocationDto> message) {
        return service.create(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = GROUP_LOCATION + UPDATE,
            containerFactory = GROUP_LOCATION + CHANGE + LISTENER_FACTORY)
    @SendTo
    public IdDto handleUpdate(ConsumerRecord<UUID, GroupLocationDto> message) {
        return service.update(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = GROUP_LOCATION + DELETE,
            containerFactory = GROUP_LOCATION + DELETE + LISTENER_FACTORY)
    public void handleDelete(ConsumerRecord<UUID, UUID> message) {
        service.delete(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = GROUP_LOCATION + READ,
            containerFactory = GROUP_LOCATION + READ + LISTENER_FACTORY)
    @SendTo
    public GroupLocationDto handleRead(ConsumerRecord<UUID, UUID> message) {
        return service.read(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = GROUP_LOCATION + VALIDATE,
            containerFactory = GROUP_LOCATION + VALIDATE + LISTENER_FACTORY)
    @SendTo
    public ValidationMessageDto handleValidate(ConsumerRecord<UUID, GroupLocationDto> message) {
        GroupLocationDto dto = message.value();
        return new ValidationMessageDto(validator.validate(dto, null));
    }

    @Override
    @KafkaListener(topics = GROUP_LOCATION + FIND,
            containerFactory = GROUP_LOCATION + FIND + LISTENER_FACTORY)
    @SendTo
    public ResponseDto<Object> handleFind(ConsumerRecord<UUID, FormDto> message) {
        return findService.find(message.key(), message.value());
    }
}