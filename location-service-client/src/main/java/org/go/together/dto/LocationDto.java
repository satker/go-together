package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.ComparingField;
import org.go.together.interfaces.Dto;

import java.util.UUID;

@Data
@EqualsAndHashCode(exclude = {"id"})
public class LocationDto implements Dto {
    private UUID id;

    @ComparingField("place")
    private PlaceDto place;

    @ComparingField(value = "route number")
    private Integer routeNumber;

    @ComparingField(value = "address", isMain = true)
    private String address;

    private Double latitude;

    private Double longitude;

    private Boolean isStart;
    private Boolean isEnd;
}
