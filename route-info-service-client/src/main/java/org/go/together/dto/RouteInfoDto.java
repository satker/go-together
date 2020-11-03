package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.ComparableDto;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class RouteInfoDto extends ComparableDto {
    private UUID id;
    private UUID startLocationId;
    private UUID endLocationId;
    private TransportType transportType;
    private Double cost;
    private Date movementDate;
    private Double movementDuration;

    @Override
    public String getMainField() {
        return transportType.getDescription();
    }
}
