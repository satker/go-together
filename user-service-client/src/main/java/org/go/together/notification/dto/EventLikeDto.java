package org.go.together.notification.dto;

import lombok.Data;
import org.go.together.interfaces.Dto;

import java.util.Set;
import java.util.UUID;

@Data
public class EventLikeDto implements Dto {
    private UUID id;
    private UUID eventId;
    private Set<SimpleUserDto> users;
}
