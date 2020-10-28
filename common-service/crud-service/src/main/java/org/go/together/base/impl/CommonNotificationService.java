package org.go.together.base.impl;

import org.go.together.base.NotificationCrudService;
import org.go.together.enums.NotificationStatus;
import org.go.together.find.impl.CommonFindService;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.Dto;
import org.go.together.interfaces.NotificationService;
import org.go.together.repository.entities.IdentifiedEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public abstract class CommonNotificationService<D extends Dto, E extends IdentifiedEntity>
        extends CommonFindService<D, E> implements NotificationCrudService<D, E> {
    protected NotificationService<D> notificationService;

    @Autowired(required = false)
    public void setNotificationService(NotificationService<D> notificationService) {
        this.notificationService = notificationService;
    }

    public void sendNotification(UUID uuid, D originalDto, D changedDto, NotificationStatus status) {
        if (changedDto instanceof ComparableDto) {
            Runnable notificationRunnable = () -> {
                String message = getNotificationMessage(originalDto, changedDto, status);
                switch (status) {
                    case CREATED -> notificationService.createNotification(uuid, originalDto, message);
                    case UPDATED, DELETED -> notificationService.updateNotification(uuid, changedDto, message);
                }
            };
            new Thread(notificationRunnable).start();
        }
    }

    public String getNotificationMessage(D originalDto, D changedDto, NotificationStatus notificationStatus) {
        String message = notificationService.getMessage(originalDto, changedDto, getServiceName(), notificationStatus);
        log.info(message);
        return message;
    }
}
