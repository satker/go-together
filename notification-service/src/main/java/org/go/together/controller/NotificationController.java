package org.go.together.controller;

import lombok.RequiredArgsConstructor;
import org.go.together.client.NotificationClient;
import org.go.together.dto.IdDto;
import org.go.together.dto.NotificationDto;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.find.controller.FindController;
import org.go.together.find.dto.ResponseDto;
import org.go.together.find.dto.form.FormDto;
import org.go.together.service.interfaces.NotificationMessageService;
import org.go.together.service.interfaces.NotificationReceiverMessageService;
import org.go.together.service.interfaces.NotificationReceiverService;
import org.go.together.service.interfaces.NotificationService;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class NotificationController extends FindController implements NotificationClient {
    private final NotificationMessageService notificationMessageService;
    private final NotificationReceiverService notificationReceiverService;
    private final NotificationReceiverMessageService notificationReceiverMessageService;
    private final NotificationService notificationService;

    @Override
    public ResponseDto<Object> find(FormDto formDto) {
        return super.find(formDto);
    }

    @Override
    public void addReceiver(UUID producerId, UUID receiverId) {
        notificationReceiverService.addReceiver(producerId, receiverId);
    }

    @Override
    public void removeReceiver(UUID producerId, UUID receiverId) {
        notificationReceiverService.removeReceiver(producerId, receiverId);
    }

    @Override
    public IdDto updateNotification(UUID producerId, NotificationMessageDto notificationMessage) {
        NotificationDto notificationByProducerId = notificationService.getNotificationByProducerId(producerId);
        notificationMessage.setNotificationId(notificationByProducerId.getId());
        return notificationMessageService.create(notificationMessage);
    }

    @Override
    public IdDto createNotification(UUID producerId, NotificationMessageDto notificationMessage) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setProducerId(producerId);
        IdDto notificationId = notificationService.create(notificationDto);
        notificationMessage.setNotificationId(notificationId.getId());
        return notificationMessageService.create(notificationMessage);
    }

    @Override
    public boolean readNotifications(UUID receiverId) {
        return notificationReceiverMessageService.readNotifications(receiverId);
    }
}
