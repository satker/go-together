package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.CommonMapper;
import org.go.together.base.Mapper;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.dto.NotificationReceiverDto;
import org.go.together.dto.NotificationReceiverMessageDto;
import org.go.together.model.NotificationMessage;
import org.go.together.model.NotificationReceiver;
import org.go.together.model.NotificationReceiverMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationReceiverMessageMapper extends CommonMapper<NotificationReceiverMessageDto, NotificationReceiverMessage> {
    private final Mapper<NotificationMessageDto, NotificationMessage> notificationMessageMapper;
    private final Mapper<NotificationReceiverDto, NotificationReceiver> notificationReceiverMapper;

    @Override
    public NotificationReceiverMessageDto toDto(NotificationReceiverMessage entity) {
        NotificationReceiverMessageDto notificationReceiverMessageDto = new NotificationReceiverMessageDto();
        notificationReceiverMessageDto.setId(entity.getId());
        notificationReceiverMessageDto.setIsRead(entity.getIsRead());
        notificationReceiverMessageDto.setNotificationReceiver(notificationReceiverMapper.entityToDto(entity.getNotificationReceiver()));
        notificationReceiverMessageDto.setNotificationMessage(notificationMessageMapper.entityToDto(entity.getNotificationMessage()));
        return notificationReceiverMessageDto;
    }

    @Override
    public NotificationReceiverMessage toEntity(NotificationReceiverMessageDto dto) {
        NotificationReceiverMessage notificationReceiverMessage = new NotificationReceiverMessage();
        notificationReceiverMessage.setId(dto.getId());
        notificationReceiverMessage.setIsRead(dto.getIsRead());
        notificationReceiverMessage.setNotificationReceiver(notificationReceiverMapper.dtoToEntity(dto.getNotificationReceiver()));
        notificationReceiverMessage.setNotificationMessage(notificationMessageMapper.dtoToEntity(dto.getNotificationMessage()));
        return notificationReceiverMessage;
    }
}
