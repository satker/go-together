package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.Dto;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotificationMessageDto extends Dto {
    private String message;
    private Date date;
    private UUID notificationId;
}
