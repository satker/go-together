package org.go.together.mapper;

import org.go.together.dto.NotificationReceiverDto;
import org.go.together.model.NotificationReceiver;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class NotificationReceiverMapper implements Mapper<NotificationReceiverDto, NotificationReceiver> {
    private final NotificationMapper notificationMapper;
    private final NotificationReceiverMessageMapper notificationReceiverMessageMapper;

    public NotificationReceiverMapper(NotificationMapper notificationMapper,
                                      NotificationReceiverMessageMapper notificationReceiverMessageMapper) {
        this.notificationMapper = notificationMapper;
        this.notificationReceiverMessageMapper = notificationReceiverMessageMapper;
    }

    @Override
    public NotificationReceiverDto entityToDto(NotificationReceiver entity) {
        NotificationReceiverDto notificationReceiverDto = new NotificationReceiverDto();
        notificationReceiverDto.setId(entity.getId());
        notificationReceiverDto.setNotification(notificationMapper.entityToDto(entity.getNotification()));
        notificationReceiverDto.setUserId(entity.getUserId());
        notificationReceiverDto.setNotificationReceiverMessages(entity.getNotificationReceiverMessages().stream()
                .map(notificationReceiverMessageMapper::entityToDto)
                .collect(Collectors.toSet()));
        return notificationReceiverDto;
    }

    @Override
    public NotificationReceiver dtoToEntity(NotificationReceiverDto dto) {
        NotificationReceiver notificationReceiver = new NotificationReceiver();
        notificationReceiver.setId(dto.getId());
        notificationReceiver.setNotification(notificationMapper.dtoToEntity(dto.getNotification()));
        notificationReceiver.setNotificationReceiverMessages(dto.getNotificationReceiverMessages().stream()
                .map(notificationReceiverMessageMapper::dtoToEntity)
                .collect(Collectors.toSet()));
        notificationReceiver.setUserId(dto.getUserId());
        return notificationReceiver;
    }
}
