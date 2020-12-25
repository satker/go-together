package org.go.together.notification.mappers.impl;

import org.go.together.dto.NotificationDto;
import org.go.together.dto.NotificationReceiverDto;
import org.go.together.notification.mappers.interfaces.ReceiverMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ReceiverMapperImpl implements ReceiverMapper {
    @Override
    public NotificationReceiverDto getReceiverDto(UUID producerId, UUID receiverId) {
        if (receiverId == null || producerId == null) {
            throw new IllegalArgumentException("Cannot send add receiver action to notification service: producerId or receiverId is null");
        }
        NotificationReceiverDto notificationReceiverDto = new NotificationReceiverDto();
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setProducerId(producerId);
        notificationReceiverDto.setNotification(notificationDto);
        notificationReceiverDto.setUserId(receiverId);
        return notificationReceiverDto;
    }
}
