package org.go.together.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDto implements ComparableDto {
    private UUID id;
    @ComparingField("country code")
    private String countryCode;
    @ComparingField(value = "country name", isMain = true)
    private String name;
}
