package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.Dto;

import java.util.Date;
import java.util.UUID;

@Data
public class MessageDto implements Dto {
    private UUID id;
    private String message;
    private double rating;
    private Date date;
    private UUID authorId;
    private MessageType messageType;
    private UUID recipientId;
}
