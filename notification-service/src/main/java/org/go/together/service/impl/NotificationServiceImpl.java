package org.go.together.service.impl;

import org.go.together.base.CommonCrudService;
import org.go.together.dto.NotificationDto;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.model.Notification;
import org.go.together.repository.interfaces.NotificationRepository;
import org.go.together.service.interfaces.NotificationService;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class NotificationServiceImpl extends CommonCrudService<NotificationDto, Notification>
        implements NotificationService {
    @Override
    public NotificationDto getNotificationByProducerId(UUID producerId) {
        return ((NotificationRepository) repository).findByProducerId(producerId)
                .map(Notification::getId)
                .map(notificationId -> super.read(notificationId))
                .orElseThrow(() -> new CannotFindEntityException("Cannot find notification by producer id: " + producerId));
    }

    public Notification getPresentedNotificationByDto(NotificationDto notificationDto) {
        Optional<Notification> notificationByProducer =
                ((NotificationRepository) repository).findByProducerId(notificationDto.getProducerId());
        return notificationByProducer.orElse(repository.findByIdOrThrow(super.create(notificationDto).getId()));
    }

    @Override
    public String getServiceName() {
        return "notifications";
    }
}
