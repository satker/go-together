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
public class LocationDto implements Dto {
    private UUID id;
    private String name;
    private String state;
    private CountryDto country;

    @Override
    public Map<String, ComparingObject> getComparingMap() {
        return ImmutableMap.<String, ComparingObject>builder()
                .put("name", ComparingObject.builder().getDtoField(this::getName).build())
                .put("state", ComparingObject.builder().getDtoField(this::getState).build())
                .put("country", ComparingObject.builder().getDtoField(this::getCountry).build())
                .build();
    }
}
