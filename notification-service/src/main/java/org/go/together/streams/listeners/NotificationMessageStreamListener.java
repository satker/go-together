package org.go.together.streams.listeners;

import lombok.RequiredArgsConstructor;
import org.go.together.dto.IdDto;
import org.go.together.dto.NotificationDto;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.message.NotificationEvent;
import org.go.together.service.interfaces.NotificationMessageService;
import org.go.together.service.interfaces.NotificationReceiverService;
import org.go.together.service.interfaces.NotificationService;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import static org.go.together.streams.interfaces.NotificationMessageStream.INPUT;

@Component
@RequiredArgsConstructor
public class NotificationMessageStreamListener {
    private final NotificationReceiverService notificationReceiverService;
    private final NotificationMessageService notificationMessageService;
    private final NotificationService notificationService;

    @StreamListener(target = INPUT)
    public void handleMessage(NotificationEvent message) {
        switch (message.getStatus()) {
            case UPDATE_MESSAGE -> updateNotification(message);
            case CREATE_MESSAGE -> createNotification(message);
            case ADD_RECEIVER -> addReceiver(message);
            case REMOVE_RECEIVER -> removeReceiver(message);
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