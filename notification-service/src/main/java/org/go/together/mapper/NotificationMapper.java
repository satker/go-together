package org.go.together.mapper;

import org.go.together.base.CommonMapper;
import org.go.together.dto.NotificationDto;
import org.go.together.model.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper extends CommonMapper<NotificationDto, Notification> {
    @Override
    public NotificationDto toDto(Notification entity) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setId(entity.getId());
        notificationDto.setProducerId(entity.getProducerId());
        return notificationDto;
    }

    @Override
    public Notification toEntity(NotificationDto dto) {
        Notification notification = new Notification();
        notification.setId(dto.getId());
        notification.setProducerId(dto.getProducerId());
        return notification;
    }
}
