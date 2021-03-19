package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.compare.ComparableDto;
import org.go.together.compare.ComparingField;

import java.util.Collection;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class GroupRouteInfoDto extends ComparableDto {
    private UUID groupId;

    @ComparingField("info routes")
    private Collection<RouteInfoDto> infoRoutes;

    @Override
    public String getMainField() {
        return groupId.toString();
    }
}
