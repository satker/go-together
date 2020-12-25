package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.compare.ComparableDto;
import org.go.together.compare.ComparingField;

@Data
@EqualsAndHashCode(callSuper = true)
public class LocationDto extends ComparableDto {

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
