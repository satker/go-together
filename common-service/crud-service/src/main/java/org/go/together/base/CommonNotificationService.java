package org.go.together.base;

import org.go.together.compare.ComparableDto;
import org.go.together.dto.Dto;
import org.go.together.enums.CrudOperation;
import org.go.together.enums.NotificationStatus;
import org.go.together.find.CommonFindService;
import org.go.together.model.IdentifiedEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public abstract class CommonNotificationService<D extends Dto, E extends IdentifiedEntity>
        extends CommonFindService<D, E> implements NotificationCrudService<D, E> {
    protected NotificationService<D> notificationService;

    @Autowired(required = false)
    public void setNotificationService(NotificationService<D> notificationService) {
        this.notificationService = notificationService;
    }

    public void sendNotification(UUID requestId, UUID uuid, D originalDto, D changedDto, CrudOperation crudOperation) {
        if (isNotifiable(changedDto)) {
            NotificationStatus status = getNotificationStatusByCrud(crudOperation);
            String message = getNotificationMessage(requestId, originalDto, changedDto, status);
            switch (status) {
                case CREATED -> notificationService.createNotification(requestId, uuid, originalDto, message);
                case UPDATED, DELETED -> notificationService.updateNotification(requestId, uuid, changedDto, message);
            }
        }
    }

    private boolean isNotifiable(D dto) {
        if (notificationService != null && dto instanceof ComparableDto) {
            ComparableDto comparableDto = (ComparableDto) dto;
            return comparableDto.getOwnerId() != null;
        }
        return false;
    }

    private NotificationStatus getNotificationStatusByCrud(CrudOperation crudOperation) {
        return switch (crudOperation) {
            case CREATE -> NotificationStatus.CREATED;
            case DELETE -> NotificationStatus.DELETED;
            case UPDATE -> NotificationStatus.UPDATED;
        };
    }

    public String getNotificationMessage(UUID requestId, D originalDto, D changedDto, NotificationStatus notificationStatus) {
        String message = notificationService.getMessage(originalDto, changedDto, getServiceName(), notificationStatus);
        log.info(message);
        return message;
    }
}
