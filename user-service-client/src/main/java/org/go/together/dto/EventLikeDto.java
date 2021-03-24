package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class EventLikeDto extends Dto {
    private UUID eventId;
    private Collection<SimpleUserDto> users;
}
