package org.go.together.client;

import org.go.together.dto.NotificationMessageDto;
import org.go.together.dto.NotificationStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@FeignClient(name = "notification-service")
public interface NotificationClient {
    @GetMapping("notifications/receivers/{receiverId}")
    Set<NotificationMessageDto> getReceiverNotifications(@PathVariable("receiverId") UUID receiverId);

    @PutMapping("notifications/receivers/{receiverId}/producers/{producerId}")
    boolean addReceiver(@PathVariable("producerId") UUID producerId,
                        @PathVariable("receiverId") UUID receiverId);

    @DeleteMapping("notifications/receivers/{receiverId}/producers/{producerId}")
    boolean removeReceiver(@PathVariable("producerId") UUID producerId,
                           @PathVariable("receiverId") UUID receiverId);

    @PostMapping("notifications/producers/{producerId}/status/{status}")
    boolean notificate(@PathVariable("producerId") UUID producerId,
                       @PathVariable("status") NotificationStatus status,
                       @RequestBody String notificationMessage);

    @PostMapping("notifications/receivers/{receiverId}")
    boolean readNotifications(@PathVariable("receiverId") UUID receiverId);
}
