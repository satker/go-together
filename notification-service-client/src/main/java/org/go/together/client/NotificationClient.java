package org.go.together.client;

import org.go.together.dto.NotificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@FeignClient(name = "notification-service")
public interface NotificationClient {
    @GetMapping("notifications/receivers/{receiverId}")
    Set<NotificationDto> getReceiverNotifications(@PathVariable("receiverId") UUID receiverId);

    @PutMapping("notifications/receivers/{receiverId}/producers/{producerId}")
    boolean addReceiver(@PathVariable("producerId") UUID producerId,
                        @PathVariable("receiverId") UUID receiverId);

    @PutMapping("notifications/receivers/{receiverId}/producers/{producerId}")
    boolean removeReceiver(@PathVariable("producerId") UUID producerId,
                           @PathVariable("receiverId") UUID receiverId);

    @PostMapping("notifications/producers/{producerId}")
    boolean notificate(@PathVariable("producerId") UUID producerId,
                       @RequestBody String notificationMessage);

    @PostMapping("notifications/receivers/{receiverId}")
    boolean readNotifications(@PathVariable("receiverId") UUID producerId);
}
