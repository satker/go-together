package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.CommonMapper;
import org.go.together.base.Mapper;
import org.go.together.dto.NotificationDto;
import org.go.together.dto.NotificationReceiverDto;
import org.go.together.model.Notification;
import org.go.together.model.NotificationReceiver;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationReceiverMapper extends CommonMapper<NotificationReceiverDto, NotificationReceiver> {
    private final Mapper<NotificationDto, Notification> notificationMapper;

    @Override
    public NotificationReceiverDto toDto(NotificationReceiver entity) {
        NotificationReceiverDto notificationReceiverDto = new NotificationReceiverDto();
        notificationReceiverDto.setId(entity.getId());
        notificationReceiverDto.setNotification(notificationMapper.entityToDto(entity.getNotification()));
        notificationReceiverDto.setUserId(entity.getUserId());
        return notificationReceiverDto;
    }

    @Override
    public NotificationReceiver toEntity(NotificationReceiverDto dto) {
        NotificationReceiver notificationReceiver = new NotificationReceiver();
        notificationReceiver.setId(dto.getId());
        notificationReceiver.setNotification(notificationMapper.dtoToEntity(dto.getNotification()));
        notificationReceiver.setUserId(dto.getUserId());
        return notificationReceiver;
    }
}
