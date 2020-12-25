package org.go.together.notification.mappers.interfaces;

import org.go.together.dto.Dto;
import org.go.together.dto.NotificationMessageDto;

import java.util.UUID;

public interface NotificationMapper {
    <D extends Dto> NotificationMessageDto getNotificationDto(UUID id, D dto, String resultMessage);
}
