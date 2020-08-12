package org.go.together.controller;

import org.go.together.client.NotificationClient;
import org.go.together.dto.IdDto;
import org.go.together.dto.NotificationDto;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.service.NotificationMessageService;
import org.go.together.service.NotificationReceiverService;
import org.go.together.service.NotificationService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.UUID;

@RestController
public class NotificationController implements NotificationClient {
    private final NotificationMessageService notificationMessageService;
    private final NotificationReceiverService notificationReceiverService;
    private final NotificationService notificationService;

    public NotificationController(NotificationMessageService notificationMessageService,
                                  NotificationReceiverService notificationReceiverService,
                                  NotificationService notificationService) {
        this.notificationMessageService = notificationMessageService;
        this.notificationReceiverService = notificationReceiverService;
        this.notificationService = notificationService;
    }

    @Override
    public Collection<NotificationMessageDto> getReceiverNotifications(UUID receiverId) {
        return notificationReceiverService.getReceiverNotifications(receiverId);
    }

    @Override
    public void addReceiver(UUID producerId, UUID receiverId) {
        notificationReceiverService.addReceiver(producerId, receiverId);
    }

    @Override
    public void removeReceiver(UUID producerId, UUID receiverId) {
        notificationReceiverService.removeReceiver(producerId, receiverId);
    }

    @Override
    public IdDto updateNotification(UUID producerId, NotificationMessageDto notificationMessage) {
        NotificationDto notificationByProducerId = notificationService.getNotificationByProducerId(producerId);
        notificationMessage.setNotificationId(notificationByProducerId.getId());
        return notificationMessageService.create(notificationMessage);
    }

    @Override
    public IdDto createNotification(UUID producerId, NotificationMessageDto notificationMessage) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setProducerId(producerId);
        IdDto notificationId = notificationService.create(notificationDto);
        notificationMessage.setNotificationId(notificationId.getId());
        return notificationMessageService.create(notificationMessage);
    }

    @Override
    public boolean readNotifications(UUID receiverId) {
        return notificationReceiverService.readNotifications(receiverId);
    }
}
