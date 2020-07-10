package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.ComparableDto;

import java.util.Set;
import java.util.UUID;

@Data
public class EventLikeDto implements ComparableDto {
    private UUID id;
    private UUID eventId;
    private Set<SimpleUserDto> users;
}
