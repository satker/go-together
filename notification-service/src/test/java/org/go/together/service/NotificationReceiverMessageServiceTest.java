package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.dto.NotificationReceiverDto;
import org.go.together.dto.NotificationReceiverMessageDto;
import org.go.together.mapper.NotificationMessageMapper;
import org.go.together.mapper.NotificationReceiverMapper;
import org.go.together.model.Notification;
import org.go.together.model.NotificationMessage;
import org.go.together.model.NotificationReceiver;
import org.go.together.model.NotificationReceiverMessage;
import org.go.together.repository.interfaces.NotificationMessageRepository;
import org.go.together.repository.interfaces.NotificationReceiverRepository;
import org.go.together.repository.interfaces.NotificationRepository;
import org.go.together.tests.CrudServiceCommonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

@ContextConfiguration(classes = RepositoryContext.class)
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9806", "port=9806"})
public class NotificationReceiverMessageServiceTest
        extends CrudServiceCommonTest<NotificationReceiverMessage, NotificationReceiverMessageDto> {
    private static final String CREATED = "Created";

    @Autowired
    private NotificationMessageRepository notificationMessageRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMessageMapper notificationMessageMapper;

    @Autowired
    private NotificationReceiverRepository notificationReceiverRepository;

    @Autowired
    private NotificationReceiverMapper notificationReceiverMapper;

    @Override
    protected NotificationReceiverMessageDto createDto() {
        NotificationReceiverMessageDto notificationReceiverMessageDto = factory.manufacturePojo(NotificationReceiverMessageDto.class);
        notificationReceiverMessageDto.setIsRead(false);

        NotificationMessage notificationMessage = notificationMessageRepository.create();
        Notification notification = notificationRepository.create();
        notification.setProducerId(UUID.randomUUID());
        Notification savedNotification = notificationRepository.save(notification);
        notificationMessage.setNotification(savedNotification);
        notificationMessage.setMessage(CREATED);
        NotificationMessage savedNotificationMessage = notificationMessageRepository.save(notificationMessage);

        NotificationMessageDto notificationMessageDto = notificationMessageMapper.entityToDto(savedNotificationMessage);
        notificationReceiverMessageDto.setNotificationMessage(notificationMessageDto);

        notificationReceiverMessageDto.setNotificationReceiver(createNotificationReceiver(notification));

        return notificationReceiverMessageDto;
    }

    private NotificationReceiverDto createNotificationReceiver(Notification notification) {
        NotificationReceiver notificationReceiver = notificationReceiverRepository.create();
        notificationReceiver.setNotification(notification);
        notificationReceiver.setUserId(UUID.randomUUID());
        return notificationReceiverMapper.entityToDto(notificationReceiver);
    }
}
