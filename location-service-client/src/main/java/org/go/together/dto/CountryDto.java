package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.compare.ComparableDto;
import org.go.together.compare.ComparingField;

@EqualsAndHashCode(callSuper = true)
@Data
public class CountryDto extends ComparableDto {
    @ComparingField("country code")
    private String countryCode;

    @ComparingField("country name")
    private String name;

    @Override
    public String getMainField() {
        return name;
    }
}
