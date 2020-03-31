package org.go.together.mapper;

import org.go.together.dto.NotificationDto;
import org.go.together.interfaces.Mapper;
import org.go.together.model.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper implements Mapper<NotificationDto, Notification> {
    @Override
    public NotificationDto entityToDto(Notification entity) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setId(entity.getId());
        notificationDto.setIsRead(entity.getIsRead());
        notificationDto.setMessage(entity.getMessage());
        return notificationDto;
    }

    @Override
    public Notification dtoToEntity(NotificationDto dto) {
        return null;
    }
}