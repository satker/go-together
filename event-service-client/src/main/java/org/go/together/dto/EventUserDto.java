package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.compare.ComparableDto;
import org.go.together.compare.ComparingField;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class EventUserDto extends ComparableDto {
    @ComparingField("user")
    private SimpleUserDto user;

    @ComparingField("user status")
    private EventUserStatus userStatus;

    private UUID eventId;

    @Override
    public UUID getOwnerId() {
        return this.getUser().getId();
    }

    @Override
    public UUID getParentId() {
        return this.getEventId();
    }

    @Override
    public String getMainField() {
        return user.getMainField();
    }
}
