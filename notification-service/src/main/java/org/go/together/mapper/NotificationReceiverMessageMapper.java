package org.go.together.mapper;

import org.go.together.dto.NotificationReceiverMessageDto;
import org.go.together.model.NotificationReceiverMessage;
import org.springframework.stereotype.Component;

@Component
public class NotificationReceiverMessageMapper implements Mapper<NotificationReceiverMessageDto, NotificationReceiverMessage> {
    private final NotificationMessageMapper notificationMessageMapper;
    private final NotificationReceiverMapper notificationReceiverMapper;

    public NotificationReceiverMessageMapper(NotificationMessageMapper notificationMessageMapper,
                                             NotificationReceiverMapper notificationReceiverMapper) {
        this.notificationMessageMapper = notificationMessageMapper;
        this.notificationReceiverMapper = notificationReceiverMapper;
    }

    @Override
    public NotificationReceiverMessageDto entityToDto(NotificationReceiverMessage entity) {
        NotificationReceiverMessageDto notificationReceiverMessageDto = new NotificationReceiverMessageDto();
        notificationReceiverMessageDto.setId(entity.getId());
        notificationReceiverMessageDto.setIsRead(entity.getIsRead());
        notificationReceiverMessageDto.setNotificationReceiver(notificationReceiverMapper.entityToDto(entity.getNotificationReceiver()));
        notificationReceiverMessageDto.setNotificationMessage(notificationMessageMapper.entityToDto(entity.getNotificationMessage()));
        return notificationReceiverMessageDto;
    }

    @Override
    public NotificationReceiverMessage dtoToEntity(NotificationReceiverMessageDto dto) {
        NotificationReceiverMessage notificationReceiverMessage = new NotificationReceiverMessage();
        notificationReceiverMessage.setId(dto.getId());
        notificationReceiverMessage.setIsRead(dto.getIsRead());
        notificationReceiverMessage.setNotificationReceiver(notificationReceiverMapper.dtoToEntity(dto.getNotificationReceiver()));
        notificationReceiverMessage.setNotificationMessage(notificationMessageMapper.dtoToEntity(dto.getNotificationMessage()));
        return notificationReceiverMessage;
    }
}
