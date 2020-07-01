package org.go.together.mapper;

import org.go.together.dto.NotificationDto;
import org.go.together.model.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    private final NotificationMessageMapper notificationMessageMapper;
    private final NotificationReceiverMapper notificationReceiverMapper;

    public NotificationMapper(NotificationMessageMapper notificationMessageMapper,
                              NotificationReceiverMapper notificationReceiverMapper) {
        this.notificationMessageMapper = notificationMessageMapper;
        this.notificationReceiverMapper = notificationReceiverMapper;
    }

    public NotificationDto entityToDto(Notification entity) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setId(entity.getId());
        notificationDto.setProducerId(entity.getProducerId());
        return notificationDto;
    }

    public Notification dtoToEntity(NotificationDto dto) {
        Notification notification = new Notification();
        notification.setId(dto.getId());
        notification.setProducerId(dto.getProducerId());
        return notification;
    }
}
