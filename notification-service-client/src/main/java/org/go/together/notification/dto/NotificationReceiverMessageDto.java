package org.go.together.notification.dto;

import lombok.Data;
import org.go.together.interfaces.Dto;

import java.util.UUID;

@Data
public class NotificationReceiverMessageDto implements Dto {
    private UUID id;
    private Boolean isRead;
    private NotificationMessageDto notificationMessage;
}