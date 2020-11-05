package org.go.together.controller;

import lombok.RequiredArgsConstructor;
import org.go.together.base.FindController;
import org.go.together.client.NotificationClient;
import org.go.together.service.interfaces.NotificationReceiverMessageService;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class NotificationController extends FindController implements NotificationClient {
    private final NotificationReceiverMessageService notificationReceiverMessageService;

    @Override
    public boolean readNotifications(UUID receiverId) {
        return notificationReceiverMessageService.readNotifications(receiverId);
    }
}
