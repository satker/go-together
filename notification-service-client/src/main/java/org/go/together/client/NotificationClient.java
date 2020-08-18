package org.go.together.client;

import org.go.together.dto.IdDto;
import org.go.together.dto.NotificationMessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@FeignClient(name = "notification-service")
public interface NotificationClient {
    @GetMapping("notifications/receivers/{receiverId}")
    Collection<NotificationMessageDto> getReceiverNotifications(@PathVariable("receiverId") UUID receiverId);

    @PutMapping("notifications/receivers/{receiverId}/producers/{producerId}")
    void addReceiver(@PathVariable("producerId") UUID producerId,
                     @PathVariable("receiverId") UUID receiverId);

    @DeleteMapping("notifications/receivers/{receiverId}/producers/{producerId}")
    void removeReceiver(@PathVariable("producerId") UUID producerId,
                        @PathVariable("receiverId") UUID receiverId);

    @PostMapping("notifications/producers/{producerId}")
    IdDto updateNotification(@PathVariable("producerId") UUID producerId,
                             @RequestBody NotificationMessageDto notificationMessage);

    @PutMapping("notifications/producers/{producerId}")
    IdDto createNotification(@PathVariable("producerId") UUID producerId,
                             @RequestBody NotificationMessageDto notificationMessage);

    @PostMapping("notifications/receivers/{receiverId}")
    boolean readNotifications(@PathVariable("receiverId") UUID receiverId);
}
