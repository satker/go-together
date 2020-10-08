package org.go.together.service;

import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.dto.NotificationDto;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.model.Notification;
import org.go.together.repository.interfaces.NotificationRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NotificationService extends CrudServiceImpl<NotificationDto, Notification> {
    public NotificationDto getNotificationByProducerId(UUID producerId) {
        return ((NotificationRepository) repository).findByProducerId(producerId)
                .map(Notification::getId)
                .map(super::read)
                .orElseThrow(() -> new CannotFindEntityException("Cannot find notification by producer id: " + producerId));
    }

    @Override
    public String getServiceName() {
        return "notification";
    }
}
