package org.go.together.base;

import org.go.together.enums.NotificationStatus;
import org.go.together.interfaces.Dto;

import java.util.UUID;

public interface NotificationService<D extends Dto> {
    String getMessage(D originalDto, D changedDto, String serviceName, NotificationStatus notificationStatus);

    void createNotification(UUID id, D dto, String resultMessage);

    void updateNotification(UUID id, D dto, String resultMessage);

    void removeReceiver(D dto);
}
