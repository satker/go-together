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

import static org.go.together.enums.UserServiceInfo.EVENT_LIKES;
import static org.go.together.interfaces.TopicKafkaPostfix.*;
import static org.go.together.kafka.consumer.constants.ConsumerBeanConfigName.LISTENER_FACTORY;

@Component
@RequiredArgsConstructor
public class EventLikesConsumer extends CommonCrudKafkaConsumer<EventLikeDto> {
    private final CrudService<EventLikeDto> service;
    private final Validator<EventLikeDto> validator;
    private final FindService<EventLikeDto> findService;

    @Override
    @KafkaListener(topics = EVENT_LIKES + CREATE,
            containerFactory = EVENT_LIKES + CHANGE + LISTENER_FACTORY)
    @SendTo
    public IdDto handleCreate(ConsumerRecord<Long, EventLikeDto> message) {
        return service.create(message.value());
    }

    @Override
    @KafkaListener(topics = EVENT_LIKES + UPDATE,
            containerFactory = EVENT_LIKES + CHANGE + LISTENER_FACTORY)
    @SendTo
    public IdDto handleUpdate(ConsumerRecord<Long, EventLikeDto> message) {
        return service.update(message.value());
    }

    @Override
    @KafkaListener(topics = EVENT_LIKES + DELETE,
            containerFactory = EVENT_LIKES + DELETE + LISTENER_FACTORY)
    public void handleDelete(ConsumerRecord<Long, UUID> message) {
        service.delete(message.value());
    }

    @Override
    @KafkaListener(topics = EVENT_LIKES + READ,
            containerFactory = EVENT_LIKES + READ + LISTENER_FACTORY)
    @SendTo
    public EventLikeDto handleRead(ConsumerRecord<Long, UUID> message) {
        return service.read(message.value());
    }

    @Override
    @KafkaListener(topics = EVENT_LIKES + VALIDATE,
            containerFactory = EVENT_LIKES + VALIDATE + LISTENER_FACTORY)
    @SendTo
    public ValidationMessageDto handleValidate(ConsumerRecord<Long, EventLikeDto> message) {
        EventLikeDto dto = message.value();
        return new ValidationMessageDto(validator.validate(dto, null));
    }

    @Override
    @KafkaListener(topics = EVENT_LIKES + FIND,
            containerFactory = EVENT_LIKES + FIND + LISTENER_FACTORY)
    @SendTo
    public ResponseDto<Object> handleFind(ConsumerRecord<Long, FormDto> message) {
        return findService.find(message.value());
    }
}
