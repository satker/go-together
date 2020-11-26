package org.go.together.mapper;

import org.go.together.base.Mapper;
import org.go.together.dto.NotificationDto;
import org.go.together.model.Notification;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NotificationMapper implements Mapper<NotificationDto, Notification> {
    @Override
    public NotificationDto entityToDto(UUID requestId, Notification entity) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setId(entity.getId());
        notificationDto.setProducerId(entity.getProducerId());
        return notificationDto;
    }

    @Override
    public Notification dtoToEntity(NotificationDto dto) {
        Notification notification = new Notification();
        notification.setId(dto.getId());
        notification.setProducerId(dto.getProducerId());
        return notification;
    }
}
