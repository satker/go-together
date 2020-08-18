package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.IdDto;
import org.go.together.dto.NotificationDto;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.model.NotificationMessage;
import org.go.together.repository.NotificationMessageRepository;
import org.go.together.repository.NotificationRepository;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = RepositoryContext.class)
public class NotificationMessageServiceTest extends CrudServiceCommonTest<NotificationMessage, NotificationMessageDto> {
    @Autowired
    private NotificationMessageService notificationMessageService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMessageRepository notificationMessageRepository;

    @Autowired
    private NotificationService notificationService;

    @Test
    public void notificate() {
        NotificationMessageDto createdNotificationMessageDto = getCreatedEntityId(dto);

        Collection<NotificationMessage> notificationMessages =
                notificationMessageRepository.findByNotificationId(createdNotificationMessageDto.getNotificationId());

        assertEquals(1, notificationMessages.size());
        assertEquals(dto.getMessage(), notificationMessages.iterator().next().getMessage());
    }

    @Test
    public void notificateWithPresented() {
        String updated = "Updated";
        NotificationMessageDto createdEntityId = getCreatedEntityId(dto);
        NotificationMessageDto notificationMessageDto = new NotificationMessageDto();
        notificationMessageDto.setDate(new Date());
        notificationMessageDto.setIsRead(null);
        notificationMessageDto.setNotificationId(createdEntityId.getNotificationId());
        notificationMessageDto.setMessage(updated);
        NotificationMessageDto createdNotificationMessageDto = getCreatedEntityId(notificationMessageDto);

        Collection<NotificationMessage> notificationMessages =
                notificationMessageRepository.findByNotificationId(createdNotificationMessageDto.getNotificationId());

        assertEquals(2, notificationMessages.size());

        Set<String> messages = Set.of(dto.getMessage(), updated);

        assertTrue(notificationMessages.stream()
                .map(NotificationMessage::getMessage)
                .allMatch(messages::contains));
    }

    @Override
    protected NotificationMessageDto createDto() {
        NotificationMessageDto notificationMessageDto = factory.manufacturePojo(NotificationMessageDto.class);
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setProducerId(UUID.randomUUID());
        IdDto notificationId = notificationService.create(notificationDto);
        notificationMessageDto.setNotificationId(notificationId.getId());
        notificationMessageDto.setIsRead(null);
        return notificationMessageDto;
    }
}
