package org.go.together.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class NotificationMessageDto {
    private UUID id;
    private String message;
    private Date date;
}
