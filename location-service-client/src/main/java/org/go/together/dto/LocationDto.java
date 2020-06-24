package org.go.together.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.interfaces.ComparingField;
import org.go.together.interfaces.Dto;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto implements Dto {
    private UUID id;
    @ComparingField(value = "name", isMain = true)
    private String name;
    @ComparingField("state")
    private String state;
    @ComparingField("country")
    private CountryDto country;
}
