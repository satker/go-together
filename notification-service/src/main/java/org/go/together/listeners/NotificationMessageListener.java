package org.go.together.listeners;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.go.together.dto.IdDto;
import org.go.together.dto.NotificationDto;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.kafka.NotificationEvent;
import org.go.together.service.interfaces.NotificationMessageService;
import org.go.together.service.interfaces.NotificationReceiverService;
import org.go.together.service.interfaces.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NotificationMessageListener {
    private final NotificationReceiverService notificationReceiverService;
    private final NotificationMessageService notificationMessageService;
    private final NotificationService notificationService;

    @KafkaListener(topics = "${kafka.groupId}")
    public void handleMessage(ConsumerRecord<UUID, NotificationEvent> message) {
        NotificationEvent value = message.value();
        switch (value.getStatus()) {
            case UPDATE_MESSAGE -> updateNotification(value);
            case CREATE_MESSAGE -> createNotification(value);
            case ADD_RECEIVER -> addReceiver(value);
            case REMOVE_RECEIVER -> removeReceiver(value);
        }
    }

    private void addReceiver(NotificationEvent message) {
        notificationReceiverService.addReceiver(message.getProducerId(), message.getReceiverId());
    }

    private void removeReceiver(NotificationEvent message) {
        notificationReceiverService.removeReceiver(message.getProducerId(), message.getReceiverId());
    }

    private void updateNotification(NotificationEvent message) {
        NotificationDto notificationByProducerId = notificationService.getNotificationByProducerId(message.getProducerId());
        final NotificationMessageDto notificationMessageDto = message.getMessage();
        notificationMessageDto.setNotificationId(notificationByProducerId.getId());
        notificationMessageService.create(notificationMessageDto);
    }

    private void createNotification(NotificationEvent message) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setProducerId(message.getProducerId());
        IdDto notificationId = notificationService.create(notificationDto);

        final NotificationMessageDto notificationMessageDto = message.getMessage();
        notificationMessageDto.setNotificationId(notificationId.getId());
        notificationMessageService.create(notificationMessageDto);
    }
}
