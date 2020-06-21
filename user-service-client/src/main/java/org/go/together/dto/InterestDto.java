package org.go.together.dto;

import com.google.common.collect.ImmutableMap;
import lombok.Data;
import org.go.together.interfaces.Dto;

import java.util.Map;
import java.util.UUID;

@Data
public class InterestDto implements Dto {
    private UUID id;
    private String name;

    @Override
    public Map<String, ComparingObject> getComparingMap() {
        return ImmutableMap.<String, ComparingObject>builder()
                .put("name", ComparingObject.builder().getDtoField(this::getName).isMain(true).build())
                .build();
    }
}
