package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.ComparingField;
import org.go.together.interfaces.Dto;

import java.util.Date;
import java.util.UUID;

@Data
public class MessageDto implements Dto {
    private UUID id;

    @ComparingField(value = "message", isMain = true)
    private String message;

    @ComparingField("rating")
    private Double rating;

    @ComparingField("date")
    private Date date;
    private UUID authorId;

    @ComparingField("message type")
    private MessageType messageType;

    private UUID recipientId;

    @Override
    public UUID getOwnerId() {
        return this.getAuthorId();
    }
}
