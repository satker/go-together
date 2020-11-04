package org.go.together.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class PlaceDto extends ComparableDto {
    @ComparingField("name")
    private String name;

    @ComparingField("state")
    private String state;

    @ComparingField("country")
    private CountryDto country;

    @JsonIgnore
    private Set<UUID> locations;

    @Override
    public String getMainField() {
        return name;
    }
}
