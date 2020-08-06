package org.go.together.mapper;

import org.go.together.model.NotificationReceiver;
import org.go.together.notification.dto.NotificationReceiverDto;
import org.springframework.stereotype.Component;

@Component
public class NotificationReceiverMapper implements Mapper<NotificationReceiverDto, NotificationReceiver> {
    private final NotificationMapper notificationMapper;

    public NotificationReceiverMapper(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    @Override
    public NotificationReceiverDto entityToDto(NotificationReceiver entity) {
        NotificationReceiverDto notificationReceiverDto = new NotificationReceiverDto();
        notificationReceiverDto.setId(entity.getId());
        notificationReceiverDto.setNotification(notificationMapper.entityToDto(entity.getNotification()));
        notificationReceiverDto.setUserId(entity.getUserId());
        return notificationReceiverDto;
    }

    @Override
    public NotificationReceiver dtoToEntity(NotificationReceiverDto dto) {
        NotificationReceiver notificationReceiver = new NotificationReceiver();
        notificationReceiver.setId(dto.getId());
        notificationReceiver.setNotification(notificationMapper.dtoToEntity(dto.getNotification()));
        notificationReceiver.setUserId(dto.getUserId());
        return notificationReceiver;
    }
}
