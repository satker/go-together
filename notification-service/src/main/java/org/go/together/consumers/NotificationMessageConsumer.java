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

import static org.go.together.enums.NotificationServiceInfo.NOTIFICATION_MESSAGE;
import static org.go.together.interfaces.TopicKafkaPostfix.*;
import static org.go.together.kafka.consumer.constants.ConsumerBeanConfigName.LISTENER_FACTORY;

@Component
@RequiredArgsConstructor
public class NotificationMessageConsumer extends CommonCrudKafkaConsumer<NotificationMessageDto> {
    private final CrudService<NotificationMessageDto> service;
    private final Validator<NotificationMessageDto> validator;
    private final FindService<NotificationMessageDto> findService;

    @Override
    @KafkaListener(topics = NOTIFICATION_MESSAGE + CREATE,
            containerFactory = NOTIFICATION_MESSAGE + CHANGE + LISTENER_FACTORY)
    @SendTo
    public IdDto handleCreate(ConsumerRecord<Long, NotificationMessageDto> message) {
        return service.create(message.value());
    }

    @Override
    @KafkaListener(topics = NOTIFICATION_MESSAGE + UPDATE,
            containerFactory = NOTIFICATION_MESSAGE + CHANGE + LISTENER_FACTORY)
    @SendTo
    public IdDto handleUpdate(ConsumerRecord<Long, NotificationMessageDto> message) {
        return service.update(message.value());
    }

    @Override
    @KafkaListener(topics = NOTIFICATION_MESSAGE + DELETE,
            containerFactory = NOTIFICATION_MESSAGE + DELETE + LISTENER_FACTORY)
    public void handleDelete(ConsumerRecord<Long, UUID> message) {
        service.delete(message.value());
    }

    @Override
    @KafkaListener(topics = NOTIFICATION_MESSAGE + READ,
            containerFactory = NOTIFICATION_MESSAGE + READ + LISTENER_FACTORY)
    @SendTo
    public NotificationMessageDto handleRead(ConsumerRecord<Long, UUID> message) {
        return service.read(message.value());
    }

    @Override
    @KafkaListener(topics = NOTIFICATION_MESSAGE + VALIDATE,
            containerFactory = NOTIFICATION_MESSAGE + VALIDATE + LISTENER_FACTORY)
    @SendTo
    public ValidationMessageDto handleValidate(ConsumerRecord<Long, NotificationMessageDto> message) {
        return new ValidationMessageDto(validator.validate(message.value(), null));
    }

    @Override
    @KafkaListener(topics = NOTIFICATION_MESSAGE + FIND,
            containerFactory = NOTIFICATION_MESSAGE + FIND + LISTENER_FACTORY)
    @SendTo
    public ResponseDto<Object> handleFind(ConsumerRecord<Long, FormDto> message) {
        return findService.find(message.value());
    }
}
