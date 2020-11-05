package org.go.together.client;

import org.go.together.base.FindClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "content-service")
public interface ContentClient extends FindClient {
}
