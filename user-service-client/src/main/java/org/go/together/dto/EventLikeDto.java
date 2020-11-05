package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class EventLikeDto extends Dto {
    private UUID eventId;
    private Set<SimpleUserDto> users;
}
