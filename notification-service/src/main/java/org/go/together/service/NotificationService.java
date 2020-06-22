package org.go.together.service;

import org.go.together.dto.NotificationMessageDto;
import org.go.together.dto.NotificationStatus;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.mapper.NotificationMessageMapper;
import org.go.together.model.Notification;
import org.go.together.model.NotificationMessage;
import org.go.together.model.NotificationReceiver;
import org.go.together.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final NotificationMessageMapper notificationMessageMapper;
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationMessageMapper notificationMessageMapper,
                               NotificationRepository notificationRepository) {
        this.notificationMessageMapper = notificationMessageMapper;
        this.notificationRepository = notificationRepository;
    }

    public Set<NotificationMessageDto> getReceiverNotifications(UUID receiverId) {
        return notificationRepository.getReceiverNotifications(receiverId).stream()
                .flatMap(notification -> notification.getNotificationMessages().stream())
                .sorted(Comparator.comparing(NotificationMessage::getDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(notificationMessageMapper::entityToDto)
                .collect(Collectors.toSet());
    }

    public boolean addReceiver(UUID producerId, UUID receiverId) {
        Optional<Notification> notificationOptional = notificationRepository.findByProducerId(producerId);
        if (notificationOptional.isPresent()) {
            Notification notification = notificationOptional.get();
            Set<NotificationReceiver> notificationReceivers = notification.getNotificationReceivers();
            boolean receiverNotPresented = notificationReceivers.stream()
                    .map(NotificationReceiver::getUserId)
                    .noneMatch(notificationReceiver -> notificationReceiver.equals(receiverId));
            if (receiverNotPresented) {
                NotificationReceiver notificationReceiver = new NotificationReceiver();
                notificationReceiver.setIsRead(false);
                notificationReceiver.setUserId(receiverId);
                notificationReceivers.add(notificationReceiver);
                notificationRepository.save(notification);
                return true;
            }
        } else {
            Notification notification = new Notification();
            notification.setProducerId(producerId);

            NotificationReceiver notificationReceiver = new NotificationReceiver();
            notificationReceiver.setIsRead(false);
            notificationReceiver.setUserId(receiverId);

            notification.setNotificationReceivers(Collections.singleton(notificationReceiver));

            notificationRepository.save(notification);
            return true;
        }
        return false;
    }

    public boolean removeReceiver(UUID producerId, UUID receiverId) {
        Optional<Notification> notificationOptional = notificationRepository.findByProducerId(producerId);
        if (notificationOptional.isPresent()) {
            Notification notification = notificationOptional.get();
            Set<NotificationReceiver> notificationReceivers = notification.getNotificationReceivers();
            boolean isRemoved = notificationReceivers
                    .removeIf(notificationReceiver -> notificationReceiver.getUserId().equals(receiverId));
            if (isRemoved) {
                notificationRepository.save(notification);
            }
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
            Set<NotificationMessage> notificationMessages = notification.getNotificationMessages();
            NotificationMessage notificationMessageEntity = new NotificationMessage();
            notificationMessageEntity.setMessage(notificationMessage);
            notificationMessageEntity.setDate(new Date());
            notificationMessages.add(notificationMessageEntity);
            notificationRepository.save(notification);
        } else {
            Notification notification = new Notification();
            notification.setProducerId(producerId);
            notification.setStatus(status);

            Set<NotificationMessage> notificationMessages = new HashSet<>();
            NotificationMessage notificationMessageEntity = new NotificationMessage();
            notificationMessageEntity.setMessage(notificationMessage);
            notificationMessageEntity.setDate(new Date());
            notificationMessages.add(notificationMessageEntity);

            notification.setNotificationMessages(notificationMessages);

            notificationRepository.save(notification);
        }
        return true;
    }

    public boolean readNotifications(UUID receiverId) {
        notificationRepository.getReceiverNotifications(receiverId).stream()
                .peek(notification -> notification.setNotificationReceivers(
                        notification.getNotificationReceivers().stream()
                                .peek(notificationReceiver -> {
                                    if (notificationReceiver.getUserId().equals(receiverId)) {
                                        notificationReceiver.setIsRead(true);
                                    }
                                })
                                .collect(Collectors.toSet())
                ))
                .forEach(notificationRepository::save);
        return true;
    }
}
