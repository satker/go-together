package org.go.together.notification.helpers.interfaces;

import org.go.together.dto.Dto;
import org.go.together.dto.NotificationMessageDto;

import java.util.Date;
import java.util.UUID;

public interface NotificationSender {
    static NotificationMessageDto getNotificationMessageDto(String resultMessage) {
        NotificationMessageDto notificationMessageDto = new NotificationMessageDto();
        notificationMessageDto.setMessage(resultMessage);
        notificationMessageDto.setDate(new Date());
        return notificationMessageDto;
    }

    <D extends Dto> void send(UUID id, D dto, String resultMessage);
}
