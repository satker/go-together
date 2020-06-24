package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.ComparingField;
import org.go.together.interfaces.Dto;

import java.util.UUID;

@Data
public class EventUserDto implements Dto {
    private UUID id;
    @ComparingField(value = "user", isMain = true)
    private SimpleUserDto user;
    @ComparingField("user status")
    private EventUserStatus userStatus;
    private UUID eventId;
}
