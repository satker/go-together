package org.go.together.client;

import org.go.together.base.FindClient;
import org.go.together.dto.SimpleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(name = "location-service")
public interface LocationClient extends FindClient {
    @GetMapping("/locations")
    Set<SimpleDto> autocompleteLocations(@RequestParam("name") String name);
}
