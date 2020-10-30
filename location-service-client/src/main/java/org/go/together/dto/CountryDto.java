package org.go.together.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDto extends ComparableDto {
    private UUID id;
    @ComparingField("country code")
    private String countryCode;
    @ComparingField("country name")
    private String name;

    @Override
    public String getMainField() {
        return name;
    }
}
