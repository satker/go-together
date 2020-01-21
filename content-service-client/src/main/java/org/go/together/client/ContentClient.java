package org.go.together.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "content-service", url = "http://localhost:8099")
public interface ContentClient {
}
