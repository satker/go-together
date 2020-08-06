package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.enums.NotificationStatus;
import org.go.together.model.Notification;
import org.go.together.model.NotificationMessage;
import org.go.together.model.NotificationReceiver;
import org.go.together.model.NotificationReceiverMessage;
import org.go.together.repository.NotificationMessageRepository;
import org.go.together.repository.NotificationReceiverMessageRepository;
import org.go.together.repository.NotificationReceiverRepository;
import org.go.together.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = RepositoryContext.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class NotificationMessageServiceTest {
    @Autowired
    private NotificationMessageService notificationMessageService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationReceiverService notificationReceiverService;

    @Autowired
    private NotificationMessageRepository notificationMessageRepository;

    @Autowired
    private NotificationReceiverRepository notificationReceiverRepository;

    @Autowired
    private NotificationReceiverMessageRepository notificationReceiverMessageRepository;

    @Test
    public void notificate() {
        UUID producerId = UUID.randomUUID();
        String message = "Created";
        NotificationStatus status = NotificationStatus.CREATED;

        Optional<Notification> producerOptional = createNotification(producerId, message, status);

        assertTrue(producerOptional.isPresent());

        Collection<NotificationMessage> notificationMessages =
                notificationMessageRepository.findByNotificationId(producerOptional.get().getId());

        assertEquals(1, notificationMessages.size());
        assertEquals(message, notificationMessages.iterator().next().getMessage());
    }

    @Test
    public void notificateWithPresented() {
        UUID producerId = UUID.randomUUID();
        String created = "Created";
        String updated = "Updated";
        createNotification(producerId, created, NotificationStatus.CREATED);
        Optional<Notification> producerOptional = createNotification(producerId, updated, NotificationStatus.UPDATED);

        assertTrue(producerOptional.isPresent());

        Collection<NotificationMessage> notificationMessages =
                notificationMessageRepository.findByNotificationId(producerOptional.get().getId());

        assertEquals(2, notificationMessages.size());

        Set<String> messages = Set.of(created, updated);

        assertTrue(notificationMessages.stream()
                .map(NotificationMessage::getMessage)
                .allMatch(messages::contains));
    }

    @Test
    public void addReceiver() {
        UUID producerId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        String created = "Created";
        createNotification(producerId, created, NotificationStatus.CREATED);
        Collection<NotificationReceiver> notificationReceivers = addReceiver(producerId, receiverId);

        assertEquals(1, notificationReceivers.size());

        NotificationReceiver notificationReceiver = notificationReceivers.iterator().next();
        validateReceiver(created, notificationReceiver);
    }

    @Test
    public void addReceiverToPresentedReceiver() {
        UUID producerId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        String created = "Created";
        createNotification(producerId, created, NotificationStatus.CREATED);
        addReceiver(producerId, UUID.randomUUID());
        Collection<NotificationReceiver> notificationReceivers = addReceiver(producerId, receiverId);

        assertEquals(2, notificationReceivers.size());

        Iterator<NotificationReceiver> notificationReceiver = notificationReceivers.iterator();

        validateReceiver(created, notificationReceiver.next());
        validateReceiver(created, notificationReceiver.next());
    }

    @Test
    public void removeReceiver() {
        UUID producerId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        String created = "Created";
        createNotification(producerId, created, NotificationStatus.CREATED);
        Collection<NotificationReceiver> notificationReceivers = removeReceiver(producerId, receiverId);

        Collection<NotificationReceiverMessage> notificationReceiverMessages =
                notificationReceiverMessageRepository.findAll();

        assertEquals(0, notificationReceivers.size());
        assertEquals(0, notificationReceiverMessages.size());
    }

    @Test
    public void removeReceiverFromPresentedReceiver() {
        UUID producerId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        String created = "Created";
        createNotification(producerId, created, NotificationStatus.CREATED);
        addReceiver(producerId, UUID.randomUUID());
        Collection<NotificationReceiver> notificationReceivers = removeReceiver(producerId, receiverId);

        Collection<NotificationReceiverMessage> notificationReceiverMessages =
                notificationReceiverMessageRepository.findAll();

        assertEquals(1, notificationReceivers.size());
        assertEquals(1, notificationReceiverMessages.size());
    }

    @Test
    public void getReceiverNotifications() {
        UUID producerId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        String created = "Created";
        createNotification(producerId, created, NotificationStatus.CREATED);
        addReceiver(producerId, receiverId);

        Collection<NotificationMessageDto> receiverNotifications = notificationMessageService.getReceiverNotifications(receiverId);

        assertEquals(1, receiverNotifications.size());
        assertEquals(created, receiverNotifications.iterator().next().getMessage());
        assertEquals(false, receiverNotifications.iterator().next().getIsRead());
    }

    @Test
    public void readNotifications() {
        UUID producerId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        String created = "Created";
        createNotification(producerId, created, NotificationStatus.CREATED);
        addReceiver(producerId, receiverId);

        boolean readResult = notificationReceiverService.readNotifications(receiverId);
        assertTrue(readResult);

        Collection<NotificationMessageDto> receiverNotifications =
                notificationMessageService.getReceiverNotifications(receiverId);

        assertEquals(1, receiverNotifications.size());
        assertEquals(created, receiverNotifications.iterator().next().getMessage());
        assertEquals(true, receiverNotifications.iterator().next().getIsRead());
    }

    private void validateReceiver(String created, NotificationReceiver notificationReceiver) {
        Set<NotificationReceiverMessage> notificationReceiverMessages = notificationReceiver.getNotificationReceiverMessages();

        assertEquals(1, notificationReceiverMessages.size());
        assertEquals(false, notificationReceiverMessages.iterator().next().getIsRead());
        assertEquals(created, notificationReceiverMessages.iterator().next().getNotificationMessage().getMessage());
    }

    private Optional<Notification> createNotification(UUID producerId, String message, NotificationStatus status) {
        NotificationMessageDto notificationMessageDto = new NotificationMessageDto();
        notificationMessageDto.setMessage(message);
        notificationMessageDto.setDate(new Date());
        boolean notificate = notificationMessageService.notificate(producerId, status, notificationMessageDto);
        assertTrue(notificate);
        return notificationRepository.findByProducerId(producerId);
    }

    private Collection<NotificationReceiver> addReceiver(UUID producerId, UUID receiverId) {
        boolean result = notificationReceiverService.addReceiver(producerId, receiverId);
        assertTrue(result);

        Optional<Notification> notificationOptional = notificationRepository.findByProducerId(producerId);

        assertTrue(notificationOptional.isPresent());

        return notificationReceiverRepository.findByNotificationId(notificationOptional.get().getId());
    }

    private Collection<NotificationReceiver> removeReceiver(UUID producerId, UUID receiverId) {
        boolean result = notificationReceiverService.removeReceiver(producerId, receiverId);
        assertTrue(result);

        Optional<Notification> notificationOptional = notificationRepository.findByProducerId(producerId);

        assertTrue(notificationOptional.isPresent());

        return notificationReceiverRepository.findByNotificationId(notificationOptional.get().getId());
    }
}
