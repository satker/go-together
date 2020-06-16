package org.go.together.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class NotificationReceiverDto {
    private UUID id;
    private UUID userId;
    private Boolean isRead;
}
