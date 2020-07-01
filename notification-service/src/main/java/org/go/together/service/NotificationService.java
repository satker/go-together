package org.go.together.service;

import org.go.together.dto.NotificationMessageDto;
import org.go.together.dto.NotificationStatus;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.mapper.NotificationMessageMapper;
import org.go.together.model.Notification;
import org.go.together.model.NotificationMessage;
import org.go.together.model.NotificationReceiver;
import org.go.together.model.NotificationReceiverMessage;
import org.go.together.repository.NotificationMessageRepository;
import org.go.together.repository.NotificationReceiverMessageRepository;
import org.go.together.repository.NotificationReceiverRepository;
import org.go.together.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final NotificationMessageMapper notificationMessageMapper;
    private final NotificationRepository notificationRepository;
    private final NotificationMessageRepository notificationMessageRepository;
    private final NotificationReceiverRepository notificationReceiverRepository;
    private final NotificationReceiverMessageRepository notificationReceiverMessageRepository;

    public NotificationService(NotificationMessageMapper notificationMessageMapper,
                               NotificationRepository notificationRepository,
                               NotificationMessageRepository notificationMessageRepository,
                               NotificationReceiverRepository notificationReceiverRepository,
                               NotificationReceiverMessageRepository notificationReceiverMessageRepository) {
        this.notificationMessageMapper = notificationMessageMapper;
        this.notificationRepository = notificationRepository;
        this.notificationMessageRepository = notificationMessageRepository;
        this.notificationReceiverRepository = notificationReceiverRepository;
        this.notificationReceiverMessageRepository = notificationReceiverMessageRepository;
    }

    public Collection<NotificationMessageDto> getReceiverNotifications(UUID receiverId) {
        return notificationReceiverRepository.findByReceiverId(receiverId)
                .stream()
                .map(NotificationReceiver::getNotificationReceiverMessages)
                .flatMap(Collection::stream)
                .map(notificationReceiverMessage -> {
                    NotificationMessageDto notificationMessageDto =
                            notificationMessageMapper.entityToDto(notificationReceiverMessage.getNotificationMessage());
                    notificationMessageDto.setIsRead(notificationReceiverMessage.getIsRead());
                    return notificationMessageDto;
                }).sorted(Comparator.comparing(NotificationMessageDto::getDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    public boolean addReceiver(UUID producerId, UUID receiverId) {
        Optional<Notification> notificationOptional = notificationRepository.findByProducerId(producerId);
        if (notificationOptional.isPresent()) {
            Notification notification = notificationOptional.get();
            Collection<NotificationReceiver> notificationReceivers =
                    notificationReceiverRepository.findByNotificationId(notification.getId());
            boolean receiverNotPresented = notificationReceivers.stream()
                    .map(NotificationReceiver::getUserId)
                    .noneMatch(notificationReceiver -> notificationReceiver.equals(receiverId));
            if (receiverNotPresented) {
                addNotificationMessageReceiver(receiverId, notification);
                return true;
            }
        } else {
            Notification notification = new Notification();
            notification.setProducerId(producerId);
            Notification savedNotification = notificationRepository.save(notification);

            addNotificationMessageReceiver(receiverId, savedNotification);
            return true;
        }
        return false;
    }

    private void addNotificationMessageReceiver(UUID receiverId, Notification savedNotification) {
        NotificationReceiver notificationReceiver = new NotificationReceiver();
        notificationReceiver.setUserId(receiverId);
        notificationReceiver.setNotification(savedNotification);
        NotificationReceiver saveNotificationReceiver = notificationReceiverRepository.save(notificationReceiver);

        Set<NotificationReceiverMessage> notificationReceiverMessages = notificationMessageRepository.findByNotificationId(savedNotification.getId()).stream()
                .map(notificationMessage -> new NotificationReceiverMessage(false, notificationMessage))
                .collect(Collectors.toSet());

        saveNotificationReceiver.setNotificationReceiverMessages(notificationReceiverMessages);
        notificationReceiverRepository.save(saveNotificationReceiver);
    }

    public boolean removeReceiver(UUID producerId, UUID receiverId) {
        Optional<Notification> notificationOptional = notificationRepository.findByProducerId(producerId);
        if (notificationOptional.isPresent()) {
            Notification notification = notificationOptional.get();
            Collection<NotificationReceiver> notificationReceivers =
                    notificationReceiverRepository.findByNotificationId(notification.getId());
            notificationReceivers
                    .removeIf(notificationReceiver -> notificationReceiver.getUserId().equals(receiverId));
        } else {
            throw new CannotFindEntityException("Cannot find notification.");
        }
        return true;
    }

    public boolean notificate(UUID producerId, NotificationStatus status, String notificationMessage) {
        Optional<Notification> notificationOptional = notificationRepository.findByProducerId(producerId);
        if (notificationOptional.isPresent()) {
            Notification notification = notificationOptional.get();
            notification.setStatus(status);
            Notification savedNotification = notificationRepository.save(notification);

            saveNotificationMessage(notificationMessage, savedNotification);

        } else {
            Notification notification = new Notification();
            notification.setProducerId(producerId);
            notification.setStatus(status);
            Notification savedNotification = notificationRepository.save(notification);

            saveNotificationMessage(notificationMessage, savedNotification);
        }
        return true;
    }

    private void saveNotificationMessage(String notificationMessage, Notification notification) {
        NotificationMessage notificationMessageEntity = new NotificationMessage();
        notificationMessageEntity.setMessage(notificationMessage);
        notificationMessageEntity.setDate(new Date());
        notificationMessageEntity.setNotification(notification);
        NotificationMessage saveNotificationMessage = notificationMessageRepository.save(notificationMessageEntity);

        Collection<NotificationReceiver> notificationReceivers =
                notificationReceiverRepository.findByNotificationId(notification.getId());
        notificationReceivers.stream()
                .peek(notificationReceiver -> {
                    Set<NotificationReceiverMessage> notificationReceiverMessages = notificationReceiver.getNotificationReceiverMessages();
                    NotificationReceiverMessage notificationReceiverMessage = new NotificationReceiverMessage(false, saveNotificationMessage);
                    notificationReceiverMessages.add(notificationReceiverMessage);
                    notificationReceiver.setNotificationReceiverMessages(notificationReceiverMessages);
                }).forEach(notificationReceiverRepository::save);
    }

    public boolean readNotifications(UUID receiverId) {
        notificationReceiverRepository.findByReceiverId(receiverId).stream()
                .map(NotificationReceiver::getNotificationReceiverMessages)
                .flatMap(Collection::stream)
                .peek(notificationReceiverMessage -> notificationReceiverMessage.setIsRead(true))
                .forEach(notificationReceiverMessageRepository::save);
        return true;
    }
}
