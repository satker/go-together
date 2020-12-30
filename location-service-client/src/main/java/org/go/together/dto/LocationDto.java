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

    @ComparingField("address")
    private String address;

    private Double latitude;

    private Double longitude;

    @Override
    public String getMainField() {
        return address;
    }
}
