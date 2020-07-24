package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.ComparableDto;

import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(exclude = {"id"})
public class GroupLocationDto implements ComparableDto {
    private UUID id;
    private UUID groupId;
    private Set<LocationDto> locations;
    private LocationCategory category;
}
