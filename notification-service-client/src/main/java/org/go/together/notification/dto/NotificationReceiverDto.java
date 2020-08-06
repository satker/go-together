package org.go.together.notification.dto;

import lombok.Data;
import org.go.together.interfaces.Dto;

import java.util.Set;
import java.util.UUID;

@Data
public class NotificationReceiverDto implements Dto {
    private UUID id;
    private UUID userId;
    private NotificationDto notification;
    private Set<NotificationReceiverMessageDto> notificationReceiverMessages;
}
