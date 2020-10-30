package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.UUID;

@Data
@EqualsAndHashCode(exclude = {"id"}, callSuper = false)
public class LocationDto extends ComparableDto {
    private UUID id;

    @ComparingField("place")
    private PlaceDto place;

    @ComparingField("route number")
    private Integer routeNumber;

    @ComparingField("address")
    private String address;

    private Double latitude;

    private Double longitude;

    private Boolean isStart;
    private Boolean isEnd;

    @Override
    public String getMainField() {
        return address;
    }
}
