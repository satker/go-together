package org.go.together.client;

import org.go.together.find.client.FindClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@FeignClient(name = "notification-service")
public interface NotificationClient extends FindClient {
    @PostMapping("notifications/receivers/{receiverId}")
    boolean readNotifications(@PathVariable("receiverId") UUID receiverId);
}
