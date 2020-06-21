package org.go.together.dto;

import com.google.common.collect.ImmutableMap;
import lombok.Data;
import org.go.together.interfaces.Dto;

import java.util.Map;
import java.util.UUID;

@Data
public class EventLocationDto implements Dto {
    private UUID id;
    private LocationDto location;
    private Integer routeNumber;
    private String address;
    private Double latitude;
    private Double longitude;
    private UUID eventId;
    private Boolean isStart;
    private Boolean isEnd;

    @Override
    public Map<String, ComparingObject> getComparingMap() {
        return ImmutableMap.<String, ComparingObject>builder()
                .put("location", ComparingObject.builder().getDtoField(this::getLocation).build())
                .put("route number", ComparingObject.builder().getDtoField(this::getRouteNumber).isMain(true).build())
                .put("address", ComparingObject.builder().getDtoField(this::getAddress).build())
                .put("latitude", ComparingObject.builder().getDtoField(this::getLatitude).build())
                .put("longitude", ComparingObject.builder().getDtoField(this::getLongitude).build())
                .build();
    }
}
