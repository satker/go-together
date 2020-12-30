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

import static org.go.together.enums.LocationServiceInfo.LOCATION;
import static org.go.together.enums.TopicKafkaPostfix.*;
import static org.go.together.kafka.consumer.constants.ConsumerBeanConfigName.LISTENER_FACTORY;

@Component
@RequiredArgsConstructor
public class LocationConsumer extends CommonCrudKafkaConsumer<LocationDto> {
    private final CrudService<LocationDto> service;
    private final Validator<LocationDto> validator;
    private final FindService<LocationDto> findService;

    @Override
    @KafkaListener(topics = LOCATION + CREATE,
            containerFactory = LOCATION + CHANGE + LISTENER_FACTORY)
    @SendTo
    public IdDto handleCreate(ConsumerRecord<UUID, LocationDto> message) {
        return service.create(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = LOCATION + UPDATE,
            containerFactory = LOCATION + CHANGE + LISTENER_FACTORY)
    @SendTo
    public IdDto handleUpdate(ConsumerRecord<UUID, LocationDto> message) {
        return service.update(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = LOCATION + DELETE,
            containerFactory = LOCATION + DELETE + LISTENER_FACTORY)
    public void handleDelete(ConsumerRecord<UUID, UUID> message) {
        service.delete(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = LOCATION + READ,
            containerFactory = LOCATION + READ + LISTENER_FACTORY)
    @SendTo
    public LocationDto handleRead(ConsumerRecord<UUID, UUID> message) {
        return service.read(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = LOCATION + VALIDATE,
            containerFactory = LOCATION + VALIDATE + LISTENER_FACTORY)
    @SendTo
    public ValidationMessageDto handleValidate(ConsumerRecord<UUID, LocationDto> message) {
        LocationDto dto = message.value();
        return new ValidationMessageDto(validator.validate(dto, null));
    }

    @Override
    @KafkaListener(topics = LOCATION + FIND,
            containerFactory = LOCATION + FIND + LISTENER_FACTORY)
    @SendTo
    public ResponseDto<Object> handleFind(ConsumerRecord<UUID, FormDto> message) {
        return findService.find(message.key(), message.value());
    }
}