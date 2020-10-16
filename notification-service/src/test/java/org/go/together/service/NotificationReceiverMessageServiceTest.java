package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.NotificationDto;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.dto.NotificationReceiverDto;
import org.go.together.dto.NotificationReceiverMessageDto;
import org.go.together.model.NotificationReceiverMessage;
import org.go.together.tests.CrudServiceCommonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

@ContextConfiguration(classes = RepositoryContext.class)
public class NotificationReceiverMessageServiceTest
        extends CrudServiceCommonTest<NotificationReceiverMessage, NotificationReceiverMessageDto> {
    private static final String CREATED = "Created";

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationMessageService notificationMessageService;

    @Autowired
    private NotificationReceiverService notificationReceiverService;

    @Override
    protected NotificationReceiverMessageDto createDto() {
        NotificationReceiverMessageDto notificationReceiverMessageDto = factory.manufacturePojo(NotificationReceiverMessageDto.class);
        notificationReceiverMessageDto.setIsRead(false);

        NotificationMessageDto notificationMessageDto = factory.manufacturePojo(NotificationMessageDto.class);
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setProducerId(UUID.randomUUID());
        UUID notificationId = notificationService.create(notificationDto).getId();
        notificationMessageDto.setNotificationId(notificationId);
        notificationMessageDto.setMessage(CREATED);

        UUID createdNotificationMessage = notificationMessageService.create(notificationMessageDto).getId();
        NotificationMessageDto readNotificationMessage = notificationMessageService.read(createdNotificationMessage);
        notificationReceiverMessageDto.setNotificationMessage(readNotificationMessage);

        notificationReceiverMessageDto.setNotificationReceiver(createNotificationReceiver(notificationId));

        return notificationReceiverMessageDto;
    }

    private NotificationReceiverDto createNotificationReceiver(UUID notificationId) {
        NotificationReceiverDto notificationReceiverDto = factory.manufacturePojo(NotificationReceiverDto.class);
        NotificationDto notificationDto = notificationService.read(notificationId);
        notificationReceiverDto.setNotification(notificationDto);
        UUID createdNotificationReceiver = notificationReceiverService.create(notificationReceiverDto).getId();
        return notificationReceiverService.read(createdNotificationReceiver);
    }
}
