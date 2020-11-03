package org.go.together.client;

import org.go.together.interfaces.FindClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "route-info-service")
public interface RouteInfoClient extends FindClient {
}
