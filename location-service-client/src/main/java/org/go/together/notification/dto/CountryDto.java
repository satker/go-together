package org.go.together.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.interfaces.ComparingField;
import org.go.together.interfaces.Dto;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDto implements Dto {
    private UUID id;
    @ComparingField("country code")
    private String countryCode;
    @ComparingField(value = "country name", isMain = true)
    private String name;
}
