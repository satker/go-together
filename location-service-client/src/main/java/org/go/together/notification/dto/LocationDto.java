package org.go.together.notification.dto;

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

    @ComparingField("latitude")
    private Double latitude;

    @ComparingField("longitude")
    private Double longitude;

    private Boolean isStart;
    private Boolean isEnd;
}
