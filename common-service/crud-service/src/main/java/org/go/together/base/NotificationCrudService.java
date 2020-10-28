package org.go.together.base;

import org.go.together.enums.NotificationStatus;
import org.go.together.interfaces.Dto;
import org.go.together.repository.entities.IdentifiedEntity;

import java.util.UUID;

public interface NotificationCrudService<D extends Dto, E extends IdentifiedEntity> {
    void sendNotification(UUID uuid, D originalDto, D changedDto, NotificationStatus status);

    String getNotificationMessage(D originalDto, D changedDto, NotificationStatus notificationStatus);
}
