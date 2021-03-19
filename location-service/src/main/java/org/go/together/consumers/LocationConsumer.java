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
import static org.go.together.interfaces.TopicKafkaPostfix.*;
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
    public IdDto handleCreate(ConsumerRecord<Long, LocationDto> message) {
        return service.create(message.value());
    }

    @Override
    @KafkaListener(topics = LOCATION + UPDATE,
            containerFactory = LOCATION + CHANGE + LISTENER_FACTORY)
    @SendTo
    public IdDto handleUpdate(ConsumerRecord<Long, LocationDto> message) {
        return service.update(message.value());
    }

    @Override
    @KafkaListener(topics = LOCATION + DELETE,
            containerFactory = LOCATION + DELETE + LISTENER_FACTORY)
    public void handleDelete(ConsumerRecord<Long, UUID> message) {
        service.delete(message.value());
    }

    @Override
    @KafkaListener(topics = LOCATION + READ,
            containerFactory = LOCATION + READ + LISTENER_FACTORY)
    @SendTo
    public LocationDto handleRead(ConsumerRecord<Long, UUID> message) {
        return service.read(message.value());
    }

    @Override
    @KafkaListener(topics = LOCATION + VALIDATE,
            containerFactory = LOCATION + VALIDATE + LISTENER_FACTORY)
    @SendTo
    public ValidationMessageDto handleValidate(ConsumerRecord<Long, LocationDto> message) {
        LocationDto dto = message.value();
        return new ValidationMessageDto(validator.validate(dto, null));
    }

    @Override
    @KafkaListener(topics = LOCATION + FIND,
            containerFactory = LOCATION + FIND + LISTENER_FACTORY)
    @SendTo
    public ResponseDto<Object> handleFind(ConsumerRecord<Long, FormDto> message) {
        return findService.find(message.value());
    }
}
