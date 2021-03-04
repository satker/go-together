package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.NotificationDto;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.enums.CrudOperation;
import org.go.together.model.Notification;
import org.go.together.model.NotificationMessage;
import org.go.together.repository.interfaces.NotificationMessageRepository;
import org.go.together.service.interfaces.NotificationService;
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
    private NotificationMessageRepository notificationMessageRepository;

    @Autowired
    private NotificationService notificationService;

    @Test
    public void notificate() {
        NotificationMessageDto createdNotificationMessageDto = getCreatedEntityId(dto);
        Notification presentedNotificationByDto = notificationService.getPresentedNotificationByDto(
                createdNotificationMessageDto.getNotification());

        Collection<NotificationMessage> notificationMessages =
                notificationMessageRepository.findByNotificationId(presentedNotificationByDto.getId());

        assertEquals(1, notificationMessages.size());
        assertEquals(dto.getMessage(), notificationMessages.iterator().next().getMessage());
    }

    @Test
    public void notificateWithPresented() {
        String updated = "Updated";
        NotificationMessageDto createdEntityId = getCreatedEntityId(dto);

        NotificationMessageDto notificationMessageDto = new NotificationMessageDto();
        notificationMessageDto.setDate(new Date());
        notificationMessageDto.setNotification(createdEntityId.getNotification());
        notificationMessageDto.setMessage(updated);
        NotificationMessageDto createdNotificationMessageDto = getCreatedEntityId(notificationMessageDto);

        Notification presentedNotificationByDto = notificationService.getPresentedNotificationByDto(
                createdNotificationMessageDto.getNotification());

        Collection<NotificationMessage> notificationMessages =
                notificationMessageRepository.findByNotificationId(presentedNotificationByDto.getId());

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
        notificationMessageDto.setNotification(notificationDto);
        return notificationMessageDto;
    }

    @Override
    protected void checkDtos(NotificationMessageDto dto, NotificationMessageDto savedObject, CrudOperation operation) {
        assertEquals(dto.getMessage(), savedObject.getMessage());
        assertEquals(dto.getDate(), savedObject.getDate());
        assertEquals(dto.getNotification().getProducerId(), savedObject.getNotification().getProducerId());
    }
}
