package org.go.together.client;

import org.go.together.dto.EventLocationDto;
import org.go.together.dto.IdDto;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.Set;
import java.util.UUID;

@FeignClient(name = "location-service", url = "http://localhost:8090")
public interface LocationClient {
    Set<EventLocationDto> getEventRoute(UUID eventId);

    Set<IdDto> saveEventRoutes(Set<EventLocationDto> eventLocationDtos);

    Set<IdDto> updateEventRoute(Set<EventLocationDto> eventLocationDtos);

    boolean deleteRoutes(Set<EventLocationDto> eventLocationDtos);

}
