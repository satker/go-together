package org.go.together.base;

import org.go.together.dto.Dto;
import org.go.together.enums.CrudOperation;
import org.go.together.enums.NotificationStatus;
import org.go.together.model.IdentifiedEntity;

import java.util.UUID;

public interface NotificationCrudService<D extends Dto, E extends IdentifiedEntity> {
    void sendNotification(UUID requestId, UUID uuid, D originalDto, D changedDto, CrudOperation crudOperation);

    String getNotificationMessage(UUID requestId, D originalDto, D changedDto, NotificationStatus notificationStatus);
}
