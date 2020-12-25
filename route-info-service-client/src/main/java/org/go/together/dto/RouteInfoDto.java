package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.compare.ComparableDto;
import org.go.together.compare.ComparingField;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class RouteInfoDto extends ComparableDto {
    private UUID startLocationId;
    private UUID endLocationId;

    @ComparingField("transport type")
    private TransportType transportType;

    @ComparingField("trip cost")
    private Double cost;

    @ComparingField("movement date")
    private Date movementDate;

    private Double movementDuration;

    @Override
    public String getMainField() {
        return transportType.getDescription();
    }
}
