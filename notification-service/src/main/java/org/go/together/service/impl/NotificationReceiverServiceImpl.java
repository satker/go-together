package org.go.together.service.impl;

import org.go.together.base.impl.CommonCrudService;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.NotificationDto;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.dto.NotificationReceiverDto;
import org.go.together.dto.NotificationReceiverMessageDto;
import org.go.together.enums.CrudOperation;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.mapper.Mapper;
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
        super.create(notificationReceiverDto);
    }

    @Override
    public void removeReceiver(UUID producerId, UUID receiverId) {
        ((NotificationReceiverRepository) repository).findByProducerId(producerId).stream()
                .filter(notificationReceiver -> notificationReceiver.getUserId().equals(receiverId))
                .map(NotificationReceiver::getId)
                .forEach(super::delete);
    }

    @Override
    public void notificateMessageReceivers(NotificationMessage entity, NotificationMessageDto dto) {
        ((NotificationReceiverRepository) repository).findByNotificationId(dto.getNotificationId()).stream()
                .map(mapper::entityToDto)
                .forEach(notificationReceiverDto -> notificateReceivers(entity, notificationReceiverDto));
    }

    private void notificateReceivers(NotificationMessage entity, NotificationReceiverDto notificationReceiverDto) {
        NotificationReceiverMessageDto notificationReceiverMessageDto = new NotificationReceiverMessageDto();
        notificationReceiverMessageDto.setIsRead(false);
        NotificationMessageDto notificationMessage = notificationMessageService.read(entity.getId());
        notificationReceiverMessageDto.setNotificationMessage(notificationMessage);
        notificationReceiverMessageDto.setNotificationReceiver(notificationReceiverDto);
        notificationReceiverMessageService.create(notificationReceiverMessageDto);
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
        return "notificationReceivers";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return Map.of("id", FieldMapper.builder()
                .currentServiceField("userId")
                .fieldClass(UUID.class).build());
    }
}
