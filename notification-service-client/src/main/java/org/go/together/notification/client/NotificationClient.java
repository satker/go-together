package org.go.together.notification.client;

import org.go.together.enums.NotificationStatus;
import org.go.together.notification.dto.NotificationMessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@FeignClient(name = "notification-service")
public interface NotificationClient {
    @GetMapping("notifications/receivers/{receiverId}")
    Collection<NotificationMessageDto> getReceiverNotifications(@PathVariable("receiverId") UUID receiverId);

    @PutMapping("notifications/receivers/{receiverId}/producers/{producerId}")
    boolean addReceiver(@PathVariable("producerId") UUID producerId,
                        @PathVariable("receiverId") UUID receiverId);

    @DeleteMapping("notifications/receivers/{receiverId}/producers/{producerId}")
    boolean removeReceiver(@PathVariable("producerId") UUID producerId,
                           @PathVariable("receiverId") UUID receiverId);

    @PostMapping("notifications/producers/{producerId}/status/{status}")
    boolean notificate(@PathVariable("producerId") UUID producerId,
                       @PathVariable("status") NotificationStatus status,
                       @RequestBody NotificationMessageDto notificationMessage);

    @PostMapping("notifications/receivers/{receiverId}")
    boolean readNotifications(@PathVariable("receiverId") UUID receiverId);
}