package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.*;
import org.go.together.model.NotificationReceiver;
import org.go.together.model.NotificationReceiverMessage;
import org.go.together.repository.NotificationReceiverMessageRepository;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = RepositoryContext.class)
public class NotificationReceiverServiceTest extends CrudServiceCommonTest<NotificationReceiver, NotificationReceiverDto> {
    private static final String CREATED = "Created";

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationMessageService notificationMessageService;

    @Autowired
    private NotificationReceiverMessageService notificationReceiverMessageService;

    @Autowired
    private NotificationReceiverMessageRepository notificationReceiverMessageRepository;

    @Test
    public void readNotifications() {
        createNotificationMessage();
        boolean readResult = ((NotificationReceiverService) crudService).readNotifications(dto.getUserId());
        assertTrue(readResult);

        Collection<NotificationMessageDto> receiverNotifications =
                ((NotificationReceiverService) crudService).getReceiverNotifications(dto.getUserId());

        assertEquals(1, receiverNotifications.size());
        assertEquals(CREATED, receiverNotifications.iterator().next().getMessage());
        assertEquals(true, receiverNotifications.iterator().next().getIsRead());
    }

    @Test
    public void getReceiverNotifications() {
        UUID receiverId = UUID.randomUUID();
        createNotificationMessage();
        ((NotificationReceiverService) crudService).addReceiver(dto.getNotification().getProducerId(), receiverId);

        Collection<NotificationMessageDto> receiverNotifications =
                ((NotificationReceiverService) crudService).getReceiverNotifications(receiverId);

        assertEquals(1, receiverNotifications.size());
        assertEquals(CREATED, receiverNotifications.iterator().next().getMessage());
        assertEquals(false, receiverNotifications.iterator().next().getIsRead());
    }

    @Test
    public void removeReceiver() {
        createNotificationMessage();
        ((NotificationReceiverService) crudService).removeReceiver(dto.getNotification().getProducerId(), dto.getUserId());

        Collection<NotificationReceiverMessage> notificationReceiverMessages = notificationReceiverMessageRepository.findAll();

        assertEquals(0, notificationReceiverMessages.size());
    }

    @Test
    public void addReceiver() {
        UUID receiverId = UUID.randomUUID();
        createNotificationMessage();
        ((NotificationReceiverService) crudService).addReceiver(dto.getNotification().getProducerId(), receiverId);

        Collection<NotificationReceiverMessage> notificationReceiverMessages = notificationReceiverMessageRepository.findAll();

        assertEquals(2, notificationReceiverMessages.size());

        assertNull(notificationReceiverMessages.iterator().next().getIsRead());
        assertEquals(CREATED, notificationReceiverMessages.iterator().next().getNotificationMessage().getMessage());
    }

    @Override
    protected NotificationReceiverDto createDto() {
        NotificationReceiverDto notificationReceiverDto = factory.manufacturePojo(NotificationReceiverDto.class);

        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setProducerId(UUID.randomUUID());
        IdDto notificationId = notificationService.create(notificationDto);


        notificationReceiverDto.setNotification(notificationService.read(notificationId.getId()));
        notificationReceiverDto.setNotificationReceiverMessages(Collections.emptySet());
        return notificationReceiverDto;
    }

    private void createNotificationMessage() {
        NotificationMessageDto notificationMessageDto = new NotificationMessageDto();
        notificationMessageDto.setMessage(CREATED);
        notificationMessageDto.setIsRead(null);
        notificationMessageDto.setNotificationId(dto.getNotification().getId());
        notificationMessageDto.setDate(new Date());
        IdDto createdNotificationMessage = notificationMessageService.create(notificationMessageDto);

        NotificationReceiverMessageDto notificationReceiverMessageDto = new NotificationReceiverMessageDto();
        notificationReceiverMessageDto.setNotificationMessage(notificationMessageService.read(createdNotificationMessage.getId()));
        notificationReceiverMessageDto.setIsRead(null);
        NotificationReceiverMessageDto read = notificationReceiverMessageService.read(notificationReceiverMessageService.create(notificationReceiverMessageDto).getId());
        dto.setNotificationReceiverMessages(Collections.singleton(read));
        getCreatedEntityId(dto);
    }
}
