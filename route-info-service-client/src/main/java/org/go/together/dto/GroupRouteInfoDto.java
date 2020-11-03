package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.ComparableDto;

import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
public class GroupRouteInfoDto extends ComparableDto {
    private UUID id;
    private UUID groupId;
    private Set<RouteInfoDto> infoRoutes;

    @Override
    public String getMainField() {
        return groupId.toString();
    }
}
