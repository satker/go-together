package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
public class EventUserDto extends ComparableDto {
    private UUID id;
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
