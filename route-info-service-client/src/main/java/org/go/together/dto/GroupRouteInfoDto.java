package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.compare.ComparableDto;

import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class GroupRouteInfoDto extends ComparableDto {
    private UUID groupId;
    private Set<RouteInfoDto> infoRoutes;

    @Override
    public String getMainField() {
        return groupId.toString();
    }
}
