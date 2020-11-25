package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.base.FindService;
import org.go.together.dto.NotificationDto;

import java.util.UUID;

public interface NotificationService extends CrudService<NotificationDto>, FindService<NotificationDto> {
    NotificationDto getNotificationByProducerId(UUID requestId, UUID producerId);
}
