package org.go.together.client;

import org.go.together.base.FindClient;
import org.go.together.dto.GroupLocationDto;
import org.go.together.dto.SimpleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;
import java.util.UUID;

@FeignClient(name = "location-service")
public interface LocationClient extends FindClient {
    @GetMapping("/locations")
    Set<SimpleDto> autocompleteLocations(@RequestParam("name") String name);

    @GetMapping("/groupLocations/{groupLocationId}")
    GroupLocationDto readGroupLocations(@PathVariable("groupLocationId") UUID routeId);
}
