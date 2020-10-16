package org.go.together.service;

import com.google.common.collect.ImmutableMap;
import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.dto.IdDto;
import org.go.together.dto.NotificationDto;
import org.go.together.dto.NotificationReceiverDto;
import org.go.together.dto.NotificationReceiverMessageDto;
import org.go.together.enums.CrudOperation;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.find.dto.FieldMapper;
import org.go.together.mapper.NotificationMapper;
import org.go.together.mapper.NotificationMessageMapper;
import org.go.together.model.Notification;
import org.go.together.model.NotificationReceiver;
import org.go.together.model.NotificationReceiverMessage;
import org.go.together.repository.interfaces.NotificationMessageRepository;
import org.go.together.repository.interfaces.NotificationReceiverRepository;
import org.go.together.repository.interfaces.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Service
public class NotificationReceiverService extends CrudServiceImpl<NotificationReceiverDto, NotificationReceiver> {
    private final NotificationReceiverMessageService notificationReceiverMessageService;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final NotificationMessageRepository notificationMessageRepository;
    private final NotificationMessageMapper notificationMessageMapper;

    protected NotificationReceiverService(NotificationReceiverMessageService notificationReceiverMessageService,
                                          NotificationRepository notificationRepository,
                                          NotificationMapper notificationMapper,
                                          NotificationMessageRepository notificationMessageRepository,
                                          NotificationMessageMapper notificationMessageMapper) {
        this.notificationReceiverMessageService = notificationReceiverMessageService;
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
                ((NotificationReceiverRepository) repository).findByProducerId(producerId);
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
        IdDto idDto = super.create(notificationReceiverDto);
        NotificationReceiverDto createdNotificationReceiverDto = super.read(idDto.getId());

        notificationMessageRepository.findByNotificationId(savedNotificationDto.getId()).stream()
                .map(notificationMessageMapper::entityToDto)
                .map(notificationMessageDto -> {
                    NotificationReceiverMessageDto notificationReceiverMessageDto = new NotificationReceiverMessageDto();
                    notificationReceiverMessageDto.setIsRead(false);
                    notificationReceiverMessageDto.setNotificationMessage(notificationMessageDto);
                    notificationReceiverMessageDto.setNotificationReceiver(createdNotificationReceiverDto);
                    return notificationReceiverMessageDto;
                })
                .forEach(notificationReceiverMessageService::create);
    }

    public void removeReceiver(UUID producerId, UUID receiverId) {
        ((NotificationReceiverRepository) repository).findByProducerId(producerId).stream()
                .filter(notificationReceiver -> notificationReceiver.getUserId().equals(receiverId))
                .map(NotificationReceiver::getId)
                .forEach(super::delete);
    }

    @Override
    protected NotificationReceiver enrichEntity(NotificationReceiver entity,
                                                NotificationReceiverDto dto,
                                                CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.DELETE) {
            notificationReceiverMessageService.getNotificationReceiverMessageIdsByReceiverId(entity.getUserId()).stream()
                    .map(NotificationReceiverMessage::getId)
                    .forEach(notificationReceiverMessageService::delete);
        }
        return entity;
    }

    @Override
    public String getServiceName() {
        return "notificationReceiver";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return ImmutableMap.<String, FieldMapper>builder()
                .put("id", FieldMapper.builder()
                        .currentServiceField("userId")
                        .fieldClass(UUID.class).build())
                .build();
    }
}
