package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.compare.ComparingField;

import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupLocationDto extends Dto {
    private UUID groupId;
    @ComparingField("locations")
    private Set<LocationDto> locations;
    private LocationCategory category;
}
