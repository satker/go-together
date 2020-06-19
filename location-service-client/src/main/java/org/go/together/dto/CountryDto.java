package org.go.together.dto;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.interfaces.Dto;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDto implements Dto {
    private UUID id;
    private String countryCode;
    private String name;

    @Override
    public Map<String, ComparingObject> getComparingMap() {
        return ImmutableMap.<String, ComparingObject>builder()
                .put("country code", ComparingObject.builder().getDtoField(this::getCountryCode).build())
                .put("country name", ComparingObject.builder().getDtoField(this::getName).build())
                .build();
    }
}
