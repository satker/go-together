package org.go.together.interfaces;

import org.go.together.enums.NotificationStatus;

import java.util.UUID;

public interface NotificationService<D extends Dto> {
    String getMessage(D dto, D anotherDto, String serviceName, NotificationStatus notificationStatus);

    void createNotification(UUID id, D dto, String resultMessage);

    void updateNotification(UUID id, D dto, String resultMessage);

    void removedReceiver(D dto);
}
