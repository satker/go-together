package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.Dto;

import java.util.UUID;

@Data
public class NotificationReceiverDto implements Dto {
    private UUID id;
    private UUID userId;
    private NotificationDto notification;
}
