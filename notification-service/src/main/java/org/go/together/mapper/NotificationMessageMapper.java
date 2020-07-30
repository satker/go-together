package org.go.together.mapper;

import org.go.together.dto.NotificationMessageDto;
import org.go.together.model.NotificationMessage;
import org.springframework.stereotype.Component;

@Component
public class NotificationMessageMapper {

    public NotificationMessageDto entityToDto(NotificationMessage entity) {
        NotificationMessageDto notificationMessageDto = new NotificationMessageDto();
        notificationMessageDto.setId(entity.getId());
        notificationMessageDto.setDate(entity.getDate());
        notificationMessageDto.setMessage(entity.getMessage());
        return notificationMessageDto;
    }


    public NotificationMessage dtoToEntity(NotificationMessageDto dto) {
        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setId(dto.getId());
        notificationMessage.setMessage(dto.getMessage());
        notificationMessage.setDate(dto.getDate());
        return notificationMessage;
    }
}
