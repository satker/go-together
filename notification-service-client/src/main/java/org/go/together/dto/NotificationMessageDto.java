package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotificationMessageDto extends Dto {
    private String message;
    private Date date;
    private NotificationDto notification;
}
