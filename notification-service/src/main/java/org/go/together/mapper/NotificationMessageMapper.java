package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.CommonMapper;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.model.Notification;
import org.go.together.model.NotificationMessage;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NotificationMessageMapper extends CommonMapper<NotificationMessageDto, NotificationMessage> {
    private final NotificationMapper notificationMapper;

    public NotificationMessageDto toDto(NotificationMessage entity) {
        NotificationMessageDto notificationMessageDto = new NotificationMessageDto();
        notificationMessageDto.setId(entity.getId());
        notificationMessageDto.setDate(entity.getDate());
        notificationMessageDto.setMessage(entity.getMessage());
        notificationMessageDto.setNotification(Optional.ofNullable(entity.getNotification())
                .map(notificationMapper::entityToDto)
                .orElse(null));
        return notificationMessageDto;
    }


    public NotificationMessage toEntity(NotificationMessageDto dto) {
        Notification notification = Optional.ofNullable(dto.getNotification())
                .map(notificationMapper::dtoToEntity)
                .orElse(null);
        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setId(dto.getId());
        notificationMessage.setMessage(dto.getMessage());
        notificationMessage.setDate(dto.getDate());
        notificationMessage.setNotification(notification);
        return notificationMessage;
    }
}
