package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.dto.NotificationDto;
import org.go.together.find.FindService;
import org.go.together.model.Notification;

import java.util.UUID;

public interface NotificationService extends CrudService<NotificationDto>, FindService<Notification> {
    NotificationDto getNotificationByProducerId(UUID producerId);
}
