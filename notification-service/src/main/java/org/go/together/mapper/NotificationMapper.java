package org.go.together.mapper;

import org.go.together.dto.NotificationDto;
import org.go.together.interfaces.Mapper;
import org.go.together.model.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper implements Mapper<NotificationDto, Notification> {
    @Override
    public NotificationDto entityToDto(Notification entity) {
        return null;
    }

    @Override
    public Notification dtoToEntity(NotificationDto dto) {
        return null;
    }
}
