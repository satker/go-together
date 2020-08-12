package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.IdDto;
import org.go.together.dto.NotificationDto;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.dto.NotificationReceiverMessageDto;
import org.go.together.model.NotificationReceiverMessage;
import org.go.together.tests.CrudServiceCommonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

@ContextConfiguration(classes = RepositoryContext.class)
public class NotificationReceiverMessageServiceTest
        extends CrudServiceCommonTest<NotificationReceiverMessage, NotificationReceiverMessageDto> {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationMessageService notificationMessageService;

    @Override
    protected NotificationReceiverMessageDto createDto() {
        NotificationReceiverMessageDto notificationReceiverMessageDto = factory.manufacturePojo(NotificationReceiverMessageDto.class);
        notificationReceiverMessageDto.setIsRead(null);

        NotificationMessageDto notificationMessageDto = factory.manufacturePojo(NotificationMessageDto.class);
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setProducerId(UUID.randomUUID());
        IdDto notificationId = notificationService.create(notificationDto);
        notificationMessageDto.setNotificationId(notificationId.getId());
        notificationMessageDto.setIsRead(null);

        IdDto idDto = notificationMessageService.create(notificationMessageDto);
        notificationReceiverMessageDto.setNotificationMessage(notificationMessageService.read(idDto.getId()));

        return notificationReceiverMessageDto;
    }
}
