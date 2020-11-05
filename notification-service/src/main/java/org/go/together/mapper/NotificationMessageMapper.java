package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.Mapper;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.model.Notification;
import org.go.together.model.NotificationMessage;
import org.go.together.repository.interfaces.NotificationRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NotificationMessageMapper implements Mapper<NotificationMessageDto, NotificationMessage> {
    private final NotificationRepository notificationRepository;

    public NotificationMessageDto entityToDto(NotificationMessage entity) {
        NotificationMessageDto notificationMessageDto = new NotificationMessageDto();
        notificationMessageDto.setId(entity.getId());
        notificationMessageDto.setDate(entity.getDate());
        notificationMessageDto.setMessage(entity.getMessage());
        notificationMessageDto.setNotificationId(Optional.ofNullable(entity.getNotification())
                .map(Notification::getId)
                .orElse(null));
        return notificationMessageDto;
    }


    public NotificationMessage dtoToEntity(NotificationMessageDto dto) {
        Notification notification = Optional.ofNullable(dto.getNotificationId())
                .map(notificationRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .orElse(null);
        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setId(dto.getId());
        notificationMessage.setMessage(dto.getMessage());
        notificationMessage.setDate(dto.getDate());
        notificationMessage.setNotification(notification);
        return notificationMessage;
    }
}
