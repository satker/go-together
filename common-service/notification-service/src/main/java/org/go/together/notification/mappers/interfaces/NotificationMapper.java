package org.go.together.notification.mappers.interfaces;

import org.go.together.dto.Dto;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.kafka.NotificationEvent;

import java.util.Date;
import java.util.UUID;

public interface NotificationMapper {
    default NotificationMessageDto getNotificationMessageDto(String resultMessage) {
        NotificationMessageDto notificationMessageDto = new NotificationMessageDto();
        notificationMessageDto.setMessage(resultMessage);
        notificationMessageDto.setDate(new Date());
        return notificationMessageDto;
    }

    <D extends Dto> NotificationEvent getNotificationEvent(UUID id, D dto, String resultMessage);
}
