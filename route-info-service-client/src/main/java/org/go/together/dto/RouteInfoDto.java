package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.compare.ComparableDto;
import org.go.together.compare.ComparingField;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class RouteInfoDto extends ComparableDto {
    @ComparingField("route")
    private LocationDto location;

    @ComparingField("transport type")
    private TransportType transportType;

    @ComparingField("trip cost")
    private Double cost;

    @ComparingField("movement date")
    private Date movementDate;

    @ComparingField("movement duration")
    private Double movementDuration;

    @ComparingField("route number")
    private Integer routeNumber;

    private Boolean isStart;

    private Boolean isEnd;

    @Override
    public String getMainField() {
        return transportType.getDescription();
    }
}
