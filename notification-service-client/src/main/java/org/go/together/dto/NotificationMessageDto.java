package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.Dto;

import java.util.Date;
import java.util.UUID;

@Data
public class NotificationMessageDto implements Dto {
    private UUID id;
    private String message;
    private Date date;
    private Boolean isRead;
    private UUID notificationId;
}
