package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.IdDto;
import org.go.together.dto.NotificationDto;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.dto.NotificationReceiverDto;
import org.go.together.model.NotificationReceiver;
import org.go.together.model.NotificationReceiverMessage;
import org.go.together.repository.NotificationMessageRepository;
import org.go.together.repository.NotificationReceiverMessageRepository;
import org.go.together.repository.NotificationRepository;
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
        createNotificationMessage();
        ((NotificationReceiverService) crudService).removeReceiver(dto.getNotification().getProducerId(), dto.getUserId());

        Collection<NotificationReceiverMessage> notificationReceiverMessages = notificationReceiverMessageRepository.findAll();

        assertEquals(0, notificationReceiverMessages.size());
    }

    @Test
    public void addReceiver() {
        getCreatedEntityId(dto);
        UUID receiverId = UUID.randomUUID();
        createNotificationMessage();
        ((NotificationReceiverService) crudService).addReceiver(dto.getNotification().getProducerId(), receiverId);

        Collection<NotificationReceiverMessage> notificationReceiverMessages = notificationReceiverMessageRepository.findAll();

        assertEquals(2, notificationReceiverMessages.size());

        assertFalse(notificationReceiverMessages.iterator().next().getIsRead());
        assertEquals(CREATED, notificationReceiverMessages.iterator().next().getNotificationMessage().getMessage());
    }

    @Override
    protected NotificationReceiverDto createDto() {
        NotificationReceiverDto notificationReceiverDto = factory.manufacturePojo(NotificationReceiverDto.class);

        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setProducerId(UUID.randomUUID());
        IdDto notificationId = notificationService.create(notificationDto);


        notificationReceiverDto.setNotification(notificationService.read(notificationId.getId()));
        return notificationReceiverDto;
    }

    private void createNotificationMessage() {
        NotificationMessageDto notificationMessageDto = new NotificationMessageDto();
        notificationMessageDto.setMessage(CREATED);
        notificationMessageDto.setIsRead(null);
        notificationMessageDto.setNotificationId(dto.getNotification().getId());
        notificationMessageDto.setDate(new Date());
        notificationMessageService.create(notificationMessageDto);
    }
}
