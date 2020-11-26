package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.Mapper;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.dto.NotificationReceiverDto;
import org.go.together.dto.NotificationReceiverMessageDto;
import org.go.together.model.NotificationMessage;
import org.go.together.model.NotificationReceiver;
import org.go.together.model.NotificationReceiverMessage;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NotificationReceiverMessageMapper implements Mapper<NotificationReceiverMessageDto, NotificationReceiverMessage> {
    private final Mapper<NotificationMessageDto, NotificationMessage> notificationMessageMapper;
    private final Mapper<NotificationReceiverDto, NotificationReceiver> notificationReceiverMapper;

    @Override
    public NotificationReceiverMessageDto entityToDto(UUID requestId, NotificationReceiverMessage entity) {
        NotificationReceiverMessageDto notificationReceiverMessageDto = new NotificationReceiverMessageDto();
        notificationReceiverMessageDto.setId(entity.getId());
        notificationReceiverMessageDto.setIsRead(entity.getIsRead());
        notificationReceiverMessageDto.setNotificationReceiver(notificationReceiverMapper.entityToDto(requestId, entity.getNotificationReceiver()));
        notificationReceiverMessageDto.setNotificationMessage(notificationMessageMapper.entityToDto(requestId, entity.getNotificationMessage()));
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
