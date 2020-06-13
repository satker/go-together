package org.go.together.service;

import org.go.together.dto.NotificationDto;
import org.go.together.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Set<NotificationDto> getReceiverNotifications(UUID receiverId) {
        return null;
    }

    public boolean addReceiver(UUID producerId, UUID receiverId) {
        return false;
    }

    public boolean removeReceiver(UUID producerId, UUID receiverId) {
        return false;
    }

    public boolean notificate(UUID producerId, String notificationMessage) {
        return false;
    }

    public boolean readNotifications(UUID producerId) {
        return false;
    }
}
