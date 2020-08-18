package org.go.together.service;

import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.dto.*;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.mapper.NotificationMapper;
import org.go.together.mapper.NotificationMessageMapper;
import org.go.together.mapper.NotificationReceiverMessageMapper;
import org.go.together.model.Notification;
import org.go.together.model.NotificationReceiver;
import org.go.together.repository.NotificationMessageRepository;
import org.go.together.repository.NotificationReceiverRepository;
import org.go.together.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationReceiverService extends CrudServiceImpl<NotificationReceiverDto, NotificationReceiver> {
    private final NotificationReceiverMessageService notificationReceiverMessageService;
    private final NotificationReceiverMessageMapper notificationReceiverMessageMapper;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final NotificationMessageRepository notificationMessageRepository;
    private final NotificationMessageMapper notificationMessageMapper;

    protected NotificationReceiverService(NotificationReceiverMessageService notificationReceiverMessageService,
                                          NotificationReceiverMessageMapper notificationReceiverMessageMapper,
                                          NotificationRepository notificationRepository,
                                          NotificationMapper notificationMapper,
                                          NotificationMessageRepository notificationMessageRepository,
                                          NotificationMessageMapper notificationMessageMapper) {
        this.notificationReceiverMessageService = notificationReceiverMessageService;
        this.notificationReceiverMessageMapper = notificationReceiverMessageMapper;
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.notificationMessageRepository = notificationMessageRepository;
        this.notificationMessageMapper = notificationMessageMapper;
    }

    public void addReceiver(UUID producerId, UUID receiverId) {
        Notification notification = notificationRepository.findByProducerId(producerId)
                .orElseThrow(() -> new CannotFindEntityException("Cannot find notification by producer id " +
                        producerId));
        Collection<NotificationReceiver> notificationReceivers =
                ((NotificationReceiverRepository) repository).findByNotificationId(notification.getId());
        boolean receiverNotPresented = notificationReceivers.stream()
                .map(NotificationReceiver::getUserId)
                .noneMatch(notificationReceiver -> notificationReceiver.equals(receiverId));
        if (receiverNotPresented) {
            addNotificationMessageReceiver(receiverId, notificationMapper.entityToDto(notification));
        }
    }

    private void addNotificationMessageReceiver(UUID receiverId, NotificationDto savedNotificationDto) {
        NotificationReceiverDto notificationReceiverDto = new NotificationReceiverDto();
        notificationReceiverDto.setUserId(receiverId);
        notificationReceiverDto.setNotification(savedNotificationDto);

        Set<NotificationReceiverMessageDto> notificationReceiverMessages =
                notificationMessageRepository.findByNotificationId(savedNotificationDto.getId()).stream()
                        .map(notificationMessageMapper::entityToDto)
                        .map(notificationMessageDto -> {
                            NotificationReceiverMessageDto notificationReceiverMessageDto = new NotificationReceiverMessageDto();
                            notificationReceiverMessageDto.setIsRead(false);
                            notificationReceiverMessageDto.setNotificationMessage(notificationMessageDto);
                            return notificationReceiverMessageDto;
                        })
                        .map(notificationReceiverMessageService::create)
                        .map(IdDto::getId)
                        .map(notificationReceiverMessageService::read)
                        .collect(Collectors.toSet());

        notificationReceiverDto.setNotificationReceiverMessages(notificationReceiverMessages);
        super.create(notificationReceiverDto);
    }

    public void removeReceiver(UUID producerId, UUID receiverId) {
        Notification notification = notificationRepository.findByProducerId(producerId)
                .orElseThrow(() -> new CannotFindEntityException("Cannot find notification by producer id " +
                        producerId));
        ((NotificationReceiverRepository) repository).findByNotificationId(notification.getId()).stream()
                .filter(notificationReceiver -> notificationReceiver.getUserId().equals(receiverId))
                .map(NotificationReceiver::getId)
                .forEach(super::delete);
    }

    public boolean readNotifications(UUID receiverId) {
        ((NotificationReceiverRepository) repository).findByReceiverId(receiverId).stream()
                .map(NotificationReceiver::getNotificationReceiverMessages)
                .flatMap(Collection::stream)
                .map(notificationReceiverMessageMapper::entityToDto)
                .peek(notificationReceiverMessage -> notificationReceiverMessage.setIsRead(true))
                .forEach(notificationReceiverMessageService::update);
        return true;
    }

    public Collection<NotificationMessageDto> getReceiverNotifications(UUID receiverId) {
        return ((NotificationReceiverRepository) repository).findByReceiverId(receiverId)
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

    @Override
    public String getServiceName() {
        return "notificationReceiver";
    }
}
