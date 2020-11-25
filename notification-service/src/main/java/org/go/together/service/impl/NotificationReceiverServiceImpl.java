package org.go.together.service.impl;

import org.go.together.base.CommonCrudService;
import org.go.together.base.Mapper;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.NotificationDto;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.dto.NotificationReceiverDto;
import org.go.together.dto.NotificationReceiverMessageDto;
import org.go.together.enums.CrudOperation;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.model.Notification;
import org.go.together.model.NotificationMessage;
import org.go.together.model.NotificationReceiver;
import org.go.together.model.NotificationReceiverMessage;
import org.go.together.repository.interfaces.NotificationReceiverRepository;
import org.go.together.repository.interfaces.NotificationRepository;
import org.go.together.service.interfaces.NotificationMessageService;
import org.go.together.service.interfaces.NotificationReceiverMessageService;
import org.go.together.service.interfaces.NotificationReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Service
public class NotificationReceiverServiceImpl extends CommonCrudService<NotificationReceiverDto, NotificationReceiver>
        implements NotificationReceiverService {
    private NotificationMessageService notificationMessageService;
    private final NotificationRepository notificationRepository;
    private final Mapper<NotificationDto, Notification> notificationMapper;
    private NotificationReceiverMessageService notificationReceiverMessageService;

    protected NotificationReceiverServiceImpl(NotificationRepository notificationRepository,
                                              Mapper<NotificationDto, Notification> notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    @Autowired
    public void setNotificationMessageService(NotificationMessageService notificationMessageService) {
        this.notificationMessageService = notificationMessageService;
    }

    @Autowired
    public void setNotificationReceiverMessageService(NotificationReceiverMessageService notificationReceiverMessageService) {
        this.notificationReceiverMessageService = notificationReceiverMessageService;
    }

    @Override
    public void addReceiver(UUID requestId, UUID producerId, UUID receiverId) {
        Notification notification = notificationRepository.findByProducerId(producerId)
                .orElseThrow(() -> new CannotFindEntityException("Cannot find notification by producer id " +
                        producerId));
        Collection<NotificationReceiver> notificationReceivers =
                ((NotificationReceiverRepository) repository).findByProducerId(producerId);
        boolean receiverNotPresented = notificationReceivers.stream()
                .map(NotificationReceiver::getUserId)
                .noneMatch(notificationReceiver -> notificationReceiver.equals(receiverId));
        if (receiverNotPresented) {
            addNotificationMessageReceiver(requestId, receiverId, notificationMapper.entityToDto(notification));
        }
    }

    private void addNotificationMessageReceiver(UUID requestId, UUID receiverId, NotificationDto savedNotificationDto) {
        NotificationReceiverDto notificationReceiverDto = new NotificationReceiverDto();
        notificationReceiverDto.setUserId(receiverId);
        notificationReceiverDto.setNotification(savedNotificationDto);
        super.create(requestId, notificationReceiverDto);
    }

    @Override
    public void removeReceiver(UUID requestId, UUID producerId, UUID receiverId) {
        ((NotificationReceiverRepository) repository).findByProducerId(producerId).stream()
                .filter(notificationReceiver -> notificationReceiver.getUserId().equals(receiverId))
                .map(NotificationReceiver::getId)
                .forEach(notificationReceiverId -> super.delete(requestId, notificationReceiverId));
    }

    @Override
    public void notificateMessageReceivers(UUID requestId, NotificationMessage entity, NotificationMessageDto dto) {
        ((NotificationReceiverRepository) repository).findByNotificationId(dto.getNotificationId()).stream()
                .map(mapper::entityToDto)
                .forEach(notificationReceiverDto -> notificateReceivers(requestId, entity, notificationReceiverDto));
    }

    private void notificateReceivers(UUID requestId, NotificationMessage entity, NotificationReceiverDto notificationReceiverDto) {
        NotificationReceiverMessageDto notificationReceiverMessageDto = new NotificationReceiverMessageDto();
        notificationReceiverMessageDto.setIsRead(false);
        NotificationMessageDto notificationMessage = notificationMessageService.read(requestId, entity.getId());
        notificationReceiverMessageDto.setNotificationMessage(notificationMessage);
        notificationReceiverMessageDto.setNotificationReceiver(notificationReceiverDto);
        notificationReceiverMessageService.create(requestId, notificationReceiverMessageDto);
    }


    @Override
    protected NotificationReceiver enrichEntity(UUID requestId,
                                                NotificationReceiver entity,
                                                NotificationReceiverDto dto,
                                                CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.DELETE) {
            notificationReceiverMessageService.getNotificationReceiverMessageIdsByReceiverId(entity.getUserId()).stream()
                    .map(NotificationReceiverMessage::getId)
                    .forEach(notificationReceiverMessageId -> notificationReceiverMessageService.delete(requestId, notificationReceiverMessageId));
        }
        return entity;
    }

    @Override
    public String getServiceName() {
        return "notificationReceivers";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return Map.of("id", FieldMapper.builder()
                .currentServiceField("userId")
                .fieldClass(UUID.class).build());
    }
}
