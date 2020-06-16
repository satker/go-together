package org.go.together.dto;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class NotificationDto {
    private UUID id;
    private UUID producerId;
    private Set<NotificationMessageDto> notificationMessages;
    private Set<NotificationReceiverDto> notificationReceivers;
}
