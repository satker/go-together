package org.go.together.consumers;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.go.together.base.CrudService;
import org.go.together.base.FindService;
import org.go.together.base.Validator;
import org.go.together.dto.*;
import org.go.together.kafka.consumer.impl.CommonCrudKafkaConsumer;
import org.go.together.model.NotificationReceiver;
import org.go.together.repository.interfaces.NotificationReceiverRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.go.together.enums.NotificationServiceInfo.NOTIFICATION_RECEIVER;
import static org.go.together.interfaces.TopicKafkaPostfix.*;
import static org.go.together.kafka.consumer.constants.ConsumerBeanConfigName.LISTENER_FACTORY;

@Component
@RequiredArgsConstructor
public class NotificationReceiverConsumer extends CommonCrudKafkaConsumer<NotificationReceiverDto> {
    private final CrudService<NotificationReceiverDto> service;
    private final Validator<NotificationReceiverDto> validator;
    private final FindService<NotificationReceiverDto> findService;
    private final NotificationReceiverRepository notificationReceiverRepository;

    @Override
    @KafkaListener(topics = NOTIFICATION_RECEIVER + CREATE,
            containerFactory = NOTIFICATION_RECEIVER + CHANGE + LISTENER_FACTORY)
    @SendTo
    public IdDto handleCreate(ConsumerRecord<Long, NotificationReceiverDto> message) {
        return service.create(message.value());
    }

    @Override
    @KafkaListener(topics = NOTIFICATION_RECEIVER + UPDATE,
            containerFactory = NOTIFICATION_RECEIVER + CHANGE + LISTENER_FACTORY)
    @SendTo
    public IdDto handleUpdate(ConsumerRecord<Long, NotificationReceiverDto> message) {
        return service.update(message.value());
    }

    @Override
    @KafkaListener(topics = NOTIFICATION_RECEIVER + DELETE,
            containerFactory = NOTIFICATION_RECEIVER + DELETE + LISTENER_FACTORY)
    public void handleDelete(ConsumerRecord<Long, UUID> message) {
        UUID producerId = message.value();
        notificationReceiverRepository.findByProducerId(producerId).stream()
                .map(NotificationReceiver::getId)
                .forEach(notificationReceiverId -> service.delete(notificationReceiverId));
    }

    @Override
    @KafkaListener(topics = NOTIFICATION_RECEIVER + READ,
            containerFactory = NOTIFICATION_RECEIVER + READ + LISTENER_FACTORY)
    @SendTo
    public NotificationReceiverDto handleRead(ConsumerRecord<Long, UUID> message) {
        return service.read(message.value());
    }

    @Override
    @KafkaListener(topics = NOTIFICATION_RECEIVER + VALIDATE,
            containerFactory = NOTIFICATION_RECEIVER + VALIDATE + LISTENER_FACTORY)
    @SendTo
    public ValidationMessageDto handleValidate(ConsumerRecord<Long, NotificationReceiverDto> message) {
        return new ValidationMessageDto(validator.validate(message.value(), null));
    }

    @Override
    @KafkaListener(topics = NOTIFICATION_RECEIVER + FIND,
            containerFactory = NOTIFICATION_RECEIVER + FIND + LISTENER_FACTORY)
    @SendTo
    public ResponseDto<Object> handleFind(ConsumerRecord<Long, FormDto> message) {
        return findService.find(message.value());
    }
}
