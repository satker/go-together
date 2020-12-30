package org.go.together.client;

import org.go.together.base.FindClient;
import org.go.together.dto.SimpleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@FeignClient(name = "route-info-service")
public interface RouteInfoClient extends FindClient {
    @GetMapping("/transportTypes")
    Collection<SimpleDto> getTransportTypes();
}
