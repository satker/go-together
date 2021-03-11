package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.NotificationDto;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.dto.NotificationReceiverDto;
import org.go.together.enums.CrudOperation;
import org.go.together.model.Notification;
import org.go.together.model.NotificationReceiver;
import org.go.together.model.NotificationReceiverMessage;
import org.go.together.repository.interfaces.NotificationMessageRepository;
import org.go.together.repository.interfaces.NotificationReceiverMessageRepository;
import org.go.together.repository.interfaces.NotificationRepository;
import org.go.together.service.interfaces.NotificationMessageService;
import org.go.together.service.interfaces.NotificationService;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ContextConfiguration(classes = RepositoryContext.class)
public class NotificationReceiverServiceTest extends CrudServiceCommonTest<NotificationReceiver, NotificationReceiverDto> {
    private static final String CREATED = "Created";

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMessageService notificationMessageService;

    @Autowired
    private NotificationMessageRepository notificationMessageRepository;

    @Autowired
    private NotificationReceiverMessageRepository notificationReceiverMessageRepository;

    @Override
    public void clean() {
        super.clean();
        notificationReceiverMessageRepository.findAll()
                .forEach(notificationReceiverMessageRepository::delete);
        notificationMessageRepository.findAll()
                .forEach(notificationMessageRepository::delete);
        notificationRepository.findAll()
                .forEach(notificationRepository::delete);
    }

    @Test
    public void removeReceiver() {
        getCreatedEntityId(dto);
        createNotificationMessage(dto);
        crudService.delete(dto.getId());

        Collection<NotificationReceiverMessage> notificationReceiverMessages = notificationReceiverMessageRepository.findAll();

        assertEquals(0, notificationReceiverMessages.size());
    }

    @Test
    public void addReceiver() {
        getCreatedEntityId(dto);
        createNotificationMessage(dto);

        Collection<NotificationReceiverMessage> notificationReceiverMessages = notificationReceiverMessageRepository.findAll();
        int receiverMessagesSize = notificationReceiverMessageRepository.findByReceiverId(dto.getUserId()).size();

        assertEquals(1, notificationReceiverMessages.size());
        assertEquals(1, receiverMessagesSize);

        createNotificationMessage(dto);

        int receiverAddMessagesSize = notificationReceiverMessageRepository.findByReceiverId(dto.getUserId()).size();
        Collection<NotificationReceiverMessage> notificationReceiverNewMessages = notificationReceiverMessageRepository.findAll();

        assertEquals(2, receiverAddMessagesSize);
        assertEquals(2, notificationReceiverNewMessages.size());

        assertFalse(notificationReceiverNewMessages.iterator().next().getIsRead());
        assertEquals(CREATED, notificationReceiverMessages.iterator().next().getNotificationMessage().getMessage());
    }

    @Override
    protected NotificationReceiverDto createDto() {
        NotificationReceiverDto notificationReceiverDto = factory.manufacturePojo(NotificationReceiverDto.class);

        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setProducerId(UUID.randomUUID());

        notificationReceiverDto.setNotification(notificationDto);
        return notificationReceiverDto;
    }

    private void createNotificationMessage(NotificationReceiverDto dto) {
        NotificationMessageDto notificationMessageDto = new NotificationMessageDto();
        notificationMessageDto.setMessage(CREATED);
        Notification notification = notificationRepository.findByProducerId(dto.getNotification().getProducerId()).orElse(null);
        notificationMessageDto.setNotification(notificationService.read(notification.getId()));
        notificationMessageDto.setDate(new Date());
        notificationMessageService.create(notificationMessageDto);
    }

    @Override
    protected void checkDtos(NotificationReceiverDto dto, NotificationReceiverDto savedObject, CrudOperation operation) {
        assertEquals(dto.getId(), savedObject.getId());
        assertEquals(dto.getUserId(), savedObject.getUserId());
        assertEquals(dto.getNotification().getProducerId(), savedObject.getNotification().getProducerId());
    }
}
