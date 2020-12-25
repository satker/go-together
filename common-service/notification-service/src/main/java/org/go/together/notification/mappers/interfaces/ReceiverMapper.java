package org.go.together.notification.mappers.interfaces;

import org.go.together.dto.NotificationReceiverDto;

import java.util.UUID;

public interface ReceiverMapper {
    NotificationReceiverDto getReceiverDto(UUID producerId, UUID receiverId);
}
