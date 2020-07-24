package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.UUID;

@Data
@EqualsAndHashCode(exclude = {"id"})
public class LocationDto implements ComparableDto {
    private UUID id;

    @ComparingField("place")
    private PlaceDto place;

    @ComparingField(value = "route number", isMain = true)
    private Integer routeNumber;

    @ComparingField("address")
    private String address;

    @ComparingField("latitude")
    private Double latitude;

    @ComparingField("longitude")
    private Double longitude;

    private Boolean isStart;
    private Boolean isEnd;
}
