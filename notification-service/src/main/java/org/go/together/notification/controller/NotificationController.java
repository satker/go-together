package org.go.together.notification.controller;

import org.go.together.enums.NotificationStatus;
import org.go.together.notification.client.NotificationClient;
import org.go.together.notification.dto.NotificationMessageDto;
import org.go.together.service.NotificationMessageService;
import org.go.together.service.NotificationReceiverService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.UUID;

@RestController
public class NotificationController implements NotificationClient {
    private final NotificationMessageService notificationMessageService;
    private final NotificationReceiverService notificationReceiverService;

    public NotificationController(NotificationMessageService notificationMessageService,
                                  NotificationReceiverService notificationReceiverService) {
        this.notificationMessageService = notificationMessageService;
        this.notificationReceiverService = notificationReceiverService;
    }

    @Override
    public Collection<NotificationMessageDto> getReceiverNotifications(UUID receiverId) {
        return notificationMessageService.getReceiverNotifications(receiverId);
    }

    @Override
    public boolean addReceiver(UUID producerId, UUID receiverId) {
        return notificationReceiverService.addReceiver(producerId, receiverId);
    }

    @Override
    public boolean removeReceiver(UUID producerId, UUID receiverId) {
        return notificationReceiverService.removeReceiver(producerId, receiverId);
    }

    @Override
    public boolean notificate(UUID producerId,
                              NotificationStatus status,
                              NotificationMessageDto notificationMessage) {
        return notificationMessageService.notificate(producerId, status, notificationMessage);
    }

    @Override
    public boolean readNotifications(UUID receiverId) {
        return notificationReceiverService.readNotifications(receiverId);
    }
}
