package org.go.together.consumers;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.go.together.base.CrudService;
import org.go.together.base.FindService;
import org.go.together.base.Validator;
import org.go.together.dto.*;
import org.go.together.kafka.impl.consumers.CommonCrudKafkaConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventLikesConsumer extends CommonCrudKafkaConsumer<EventLikeDto> {
    private final CrudService<EventLikeDto> service;
    private final Validator<EventLikeDto> validator;
    private final FindService<EventLikeDto> findService;

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.UserServiceInfo).EVENT_LIKES.getDescription()" +
            ".concat(T(org.go.together.enums.TopicKafkaPostfix).CREATE.getDescription())}",
            containerFactory = "eventLikesChangeListenerContainerFactory"
    )
    @SendTo
    public IdDto handleCreate(ConsumerRecord<UUID, EventLikeDto> message) {
        return service.create(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.UserServiceInfo).EVENT_LIKES.getDescription()" +
            ".concat(T(org.go.together.enums.TopicKafkaPostfix).UPDATE.getDescription())}",
            containerFactory = "eventLikesChangeListenerContainerFactory"
    )
    @SendTo
    public IdDto handleUpdate(ConsumerRecord<UUID, EventLikeDto> message) {
        return service.update(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.UserServiceInfo).EVENT_LIKES.getDescription()" +
            ".concat(T(org.go.together.enums.TopicKafkaPostfix).DELETE.getDescription())}",
            containerFactory = "eventLikesDeleteListenerContainerFactory")
    public void handleDelete(ConsumerRecord<UUID, UUID> message) {
        service.delete(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.UserServiceInfo).EVENT_LIKES.getDescription()" +
            ".concat(T(org.go.together.enums.TopicKafkaPostfix).READ.getDescription())}",
            containerFactory = "eventLikesReadListenerContainerFactory")
    @SendTo
    public EventLikeDto handleRead(ConsumerRecord<UUID, UUID> message) {
        return service.read(message.key(), message.value());
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.UserServiceInfo).EVENT_LIKES.getDescription()" +
            ".concat(T(org.go.together.enums.TopicKafkaPostfix).VALIDATE.getDescription())}",
            containerFactory = "eventLikesValidateListenerContainerFactory")
    @SendTo
    public ValidationMessageDto handleValidate(ConsumerRecord<UUID, EventLikeDto> message) {
        EventLikeDto dto = message.value();
        return new ValidationMessageDto(validator.validate(dto, null));
    }

    @Override
    @KafkaListener(topics = "#{T(org.go.together.enums.UserServiceInfo).EVENT_LIKES.getDescription()" +
            ".concat(T(org.go.together.enums.TopicKafkaPostfix).FIND.getDescription())}",
            containerFactory = "findListenerContainerFactory")
    @SendTo
    public ResponseDto<Object> handleFind(ConsumerRecord<UUID, FormDto> message) {
        return findService.find(message.key(), message.value());
    }
}
