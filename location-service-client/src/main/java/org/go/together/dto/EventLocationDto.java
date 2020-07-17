package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.UUID;

@Data
@EqualsAndHashCode(exclude = {"id"})
public class EventLocationDto implements ComparableDto {
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
