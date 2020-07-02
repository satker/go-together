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
public class LocationDto implements ComparableDto {
    private UUID id;
    @ComparingField(value = "name", isMain = true)
    private String name;
    @ComparingField("state")
    private String state;
    @ComparingField("country")
    private CountryDto country;
}
