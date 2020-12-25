package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotificationReceiverDto extends Dto {
    private UUID userId;
    private NotificationDto notification;
}
