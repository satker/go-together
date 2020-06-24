package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.ComparingField;
import org.go.together.interfaces.Dto;

import java.util.UUID;

@Data
public class EventLocationDto implements Dto {
    private UUID id;

    @ComparingField("location")
    private LocationDto location;

    @ComparingField(value = "route number", isMain = true)
    private Integer routeNumber;

    @ComparingField("address")
    private String address;

    @ComparingField("latitude")
    private Double latitude;

    @ComparingField("longitude")
    private Double longitude;

    private UUID eventId;
    private Boolean isStart;
    private Boolean isEnd;
}
