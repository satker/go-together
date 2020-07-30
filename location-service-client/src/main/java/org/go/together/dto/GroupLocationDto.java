package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(exclude = {"id"})
public class GroupLocationDto implements ComparableDto {
    private UUID id;
    private UUID groupId;
    @ComparingField("locations")
    private Set<LocationDto> locations;
    private LocationCategory category;
}
