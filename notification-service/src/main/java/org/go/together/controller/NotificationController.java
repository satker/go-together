package org.go.together.controller;

import org.go.together.client.NotificationClient;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.dto.NotificationStatus;
import org.go.together.service.NotificationService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.UUID;

@RestController
public class NotificationController implements NotificationClient {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public Collection<NotificationMessageDto> getReceiverNotifications(UUID receiverId) {
        return notificationService.getReceiverNotifications(receiverId);
    }

    @Override
    public boolean addReceiver(UUID producerId, UUID receiverId) {
        return notificationService.addReceiver(producerId, receiverId);
    }

    @Override
    public boolean removeReceiver(UUID producerId, UUID receiverId) {
        return notificationService.removeReceiver(producerId, receiverId);
    }

    @Override
    public boolean notificate(UUID producerId, NotificationStatus status, String notificationMessage) {
        return notificationService.notificate(producerId, status, notificationMessage);
    }

    @Override
    public boolean readNotifications(UUID receiverId) {
        return notificationService.readNotifications(receiverId);
    }
}
