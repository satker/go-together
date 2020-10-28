package org.go.together.controller;

import lombok.RequiredArgsConstructor;
import org.go.together.client.NotificationClient;
import org.go.together.find.controller.FindController;
import org.go.together.find.dto.ResponseDto;
import org.go.together.find.dto.form.FormDto;
import org.go.together.service.interfaces.NotificationReceiverMessageService;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class NotificationController extends FindController implements NotificationClient {
    private final NotificationReceiverMessageService notificationReceiverMessageService;

    @Override
    public ResponseDto<Object> find(FormDto formDto) {
        return super.find(formDto);
    }

    @Override
    public boolean readNotifications(UUID receiverId) {
        return notificationReceiverMessageService.readNotifications(receiverId);
    }
}
