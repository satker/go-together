package org.go.together.notification.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.go.together.interfaces.ComparingField;
import org.go.together.interfaces.Dto;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
public class PlaceDto implements Dto {
    private UUID id;
    @ComparingField(value = "name", isMain = true)
    private String name;
    @ComparingField("state")
    private String state;
    @ComparingField("country")
    private CountryDto country;

    @JsonIgnore
    private Set<UUID> locations;
}
