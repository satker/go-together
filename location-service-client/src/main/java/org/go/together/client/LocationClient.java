package org.go.together.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "location-service", url = "http://localhost:8090")
public interface LocationClient {
}
