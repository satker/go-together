package org.go.together.base;

import org.go.together.dto.Dto;
import org.go.together.enums.NotificationStatus;

import java.util.UUID;

public interface NotificationService<D extends Dto> {
    String getMessage(D originalDto, D changedDto, String serviceName, NotificationStatus notificationStatus);

    void createNotification(UUID requestId, UUID id, D dto, String resultMessage);

    void updateNotification(UUID requestId, UUID id, D dto, String resultMessage);

    void removeReceiver(UUID requestId, D dto);
}
