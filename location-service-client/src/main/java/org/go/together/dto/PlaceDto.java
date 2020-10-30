package org.go.together.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"}, callSuper = false)
public class PlaceDto extends ComparableDto {
    private UUID id;
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
