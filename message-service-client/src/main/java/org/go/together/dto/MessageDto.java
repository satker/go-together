package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.compare.ComparableDto;
import org.go.together.compare.ComparingField;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class MessageDto extends ComparableDto {
    @ComparingField("message")
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

    @Override
    public UUID getParentId() {
        return getId();
    }

    @Override
    public String getMainField() {
        return message;
    }
}
