package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.Dto;

import java.util.UUID;

@Data
public class EventLocationDto implements Dto {
    private UUID id;
    private LocationDto location;
    private int routeNumber;
    private String address;
    private Double latitude;
    private Double longitude;
    private UUID eventId;
    private Boolean isStart;
    private Boolean isEnd;
}
