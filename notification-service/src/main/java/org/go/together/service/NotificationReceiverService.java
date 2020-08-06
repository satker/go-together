package org.go.together.service;

import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.dto.IdDto;
import org.go.together.dto.NotificationDto;
import org.go.together.dto.NotificationReceiverDto;
import org.go.together.dto.NotificationReceiverMessageDto;
import org.go.together.find.dto.FieldMapper;
import org.go.together.mapper.NotificationMapper;
import org.go.together.mapper.NotificationMessageMapper;
import org.go.together.mapper.NotificationReceiverMessageMapper;
import org.go.together.model.Notification;
import org.go.together.model.NotificationReceiver;
import org.go.together.repository.NotificationMessageRepository;
import org.go.together.repository.NotificationReceiverRepository;
import org.go.together.repository.NotificationRepository;
import org.go.together.repository.exceptions.CannotFindEntityException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationReceiverService extends CrudServiceImpl<NotificationReceiverDto, NotificationReceiver> {
    private final NotificationReceiverMessageService notificationReceiverMessageService;
    private final NotificationReceiverMessageMapper notificationReceiverMessageMapper;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;
    private final NotificationMessageRepository notificationMessageRepository;
    private final NotificationMessageMapper notificationMessageMapper;

    protected NotificationReceiverService(NotificationReceiverMessageService notificationReceiverMessageService,
                                          NotificationReceiverMessageMapper notificationReceiverMessageMapper,
                                          NotificationRepository notificationRepository,
                                          NotificationService notificationService,
                                          NotificationMapper notificationMapper,
                                          NotificationMessageRepository notificationMessageRepository,
                                          NotificationMessageMapper notificationMessageMapper) {
        this.notificationReceiverMessageService = notificationReceiverMessageService;
        this.notificationReceiverMessageMapper = notificationReceiverMessageMapper;
        this.notificationRepository = notificationRepository;
        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
        this.notificationMessageRepository = notificationMessageRepository;
        this.notificationMessageMapper = notificationMessageMapper;
    }

    public boolean addReceiver(UUID producerId, UUID receiverId) {
        Optional<Notification> notificationOptional = notificationRepository.findByProducerId(producerId);
        if (notificationOptional.isPresent()) {
            Notification notification = notificationOptional.get();
            Collection<NotificationReceiver> notificationReceivers =
                    ((NotificationReceiverRepository) repository).findByNotificationId(notification.getId());
            boolean receiverNotPresented = notificationReceivers.stream()
                    .map(NotificationReceiver::getUserId)
                    .noneMatch(notificationReceiver -> notificationReceiver.equals(receiverId));
            if (receiverNotPresented) {
                addNotificationMessageReceiver(receiverId, notificationMapper.entityToDto(notification));
                return true;
            }
        } else {
            NotificationDto notification = new NotificationDto();
            notification.setProducerId(producerId);
            IdDto savedNotificationId = notificationService.create(notification);

            addNotificationMessageReceiver(receiverId, notificationService.read(savedNotificationId.getId()));
            return true;
        }
        return false;
    }

    private void addNotificationMessageReceiver(UUID receiverId, NotificationDto savedNotificationDto) {
        NotificationReceiverDto notificationReceiverDto = new NotificationReceiverDto();
        notificationReceiverDto.setUserId(receiverId);
        notificationReceiverDto.setNotification(savedNotificationDto);
        IdDto saveNotificationReceiverId = super.create(notificationReceiverDto);
        NotificationReceiverDto receiverDto = super.read(saveNotificationReceiverId.getId());

        Set<NotificationReceiverMessageDto> notificationReceiverMessages = notificationMessageRepository.findByNotificationId(savedNotificationDto.getId()).stream()
                .map(notificationMessageMapper::entityToDto)
                .map(notificationMessageDto -> {
                    NotificationReceiverMessageDto notificationReceiverMessageDto = new NotificationReceiverMessageDto();
                    notificationReceiverMessageDto.setIsRead(false);
                    notificationReceiverMessageDto.setNotificationMessage(notificationMessageDto);
                    return notificationReceiverMessageDto;
                })
                .collect(Collectors.toSet());

        receiverDto.setNotificationReceiverMessages(notificationReceiverMessages);
        super.create(receiverDto);
    }

    public boolean removeReceiver(UUID producerId, UUID receiverId) {
        Optional<Notification> notificationOptional = notificationRepository.findByProducerId(producerId);
        if (notificationOptional.isPresent()) {
            Notification notification = notificationOptional.get();
            ((NotificationReceiverRepository) repository).findByNotificationId(notification.getId()).stream()
                    .filter(notificationReceiver -> !notificationReceiver.getUserId().equals(receiverId))
                    .map(mapper::entityToDto)
                    .forEach(super::create);
        } else {
            throw new CannotFindEntityException("Cannot find notification.");
        }
        return true;
    }

    public boolean readNotifications(UUID receiverId) {
        ((NotificationReceiverRepository) repository).findByReceiverId(receiverId).stream()
                .map(NotificationReceiver::getNotificationReceiverMessages)
                .flatMap(Collection::stream)
                .map(notificationReceiverMessageMapper::entityToDto)
                .peek(notificationReceiverMessage -> notificationReceiverMessage.setIsRead(true))
                .forEach(notificationReceiverMessageService::create);
        return true;
    }

    @Override
    public String getServiceName() {
        return "notificationReceiver";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return null;
    }
}
