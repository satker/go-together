package org.go.together.notification.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.ComparingField;
import org.go.together.interfaces.Dto;

import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(exclude = {"id"})
public class GroupLocationDto implements Dto {
    private UUID id;
    private UUID groupId;
    @ComparingField("locations")
    private Set<LocationDto> locations;
    private LocationCategory category;
}
