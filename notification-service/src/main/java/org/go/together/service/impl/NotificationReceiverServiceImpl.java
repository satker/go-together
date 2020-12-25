package org.go.together.service.impl;

import org.go.together.base.CommonCrudService;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.dto.NotificationReceiverDto;
import org.go.together.dto.NotificationReceiverMessageDto;
import org.go.together.enums.CrudOperation;
import org.go.together.model.Notification;
import org.go.together.model.NotificationMessage;
import org.go.together.model.NotificationReceiver;
import org.go.together.model.NotificationReceiverMessage;
import org.go.together.repository.interfaces.NotificationMessageRepository;
import org.go.together.repository.interfaces.NotificationReceiverRepository;
import org.go.together.service.interfaces.NotificationMessageService;
import org.go.together.service.interfaces.NotificationReceiverMessageService;
import org.go.together.service.interfaces.NotificationReceiverService;
import org.go.together.service.interfaces.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

import static org.go.together.enums.NotificationServiceInfo.NOTIFICATION_RECEIVER;

@Service
public class NotificationReceiverServiceImpl extends CommonCrudService<NotificationReceiverDto, NotificationReceiver>
        implements NotificationReceiverService {
    private NotificationMessageService notificationMessageService;
    private final NotificationMessageRepository notificationMessageRepository;
    private NotificationReceiverMessageService notificationReceiverMessageService;
    private final NotificationService notificationService;

    protected NotificationReceiverServiceImpl(NotificationMessageRepository notificationMessageRepository,
                                              NotificationService notificationService) {
        this.notificationService = notificationService;
        this.notificationMessageRepository = notificationMessageRepository;
    }

    @Autowired
    public void setNotificationMessageService(NotificationMessageService notificationMessageService) {
        this.notificationMessageService = notificationMessageService;
    }

    @Autowired
    public void setNotificationReceiverMessageService(NotificationReceiverMessageService notificationReceiverMessageService) {
        this.notificationReceiverMessageService = notificationReceiverMessageService;
    }

    private void enrichByMessages(UUID requestId, NotificationMessage notificationMessage, NotificationReceiver entity) {
        NotificationReceiverMessageDto notificationReceiverMessageDto = new NotificationReceiverMessageDto();
        notificationReceiverMessageDto.setIsRead(false);
        NotificationMessageDto notificationMessageDto = notificationMessageService.read(requestId, notificationMessage.getId());
        notificationReceiverMessageDto.setNotificationMessage(notificationMessageDto);
        NotificationReceiverDto notificationReceiverDto = mapper.entityToDto(requestId, entity);
        notificationReceiverMessageDto.setNotificationReceiver(notificationReceiverDto);
        notificationReceiverMessageService.create(requestId, notificationReceiverMessageDto);
    }

    @Override
    public void notificateMessageReceivers(UUID requestId, NotificationMessage entity) {
        ((NotificationReceiverRepository) repository).findByNotificationId(entity.getNotification().getId())
                .forEach(notificationReceiver -> enrichByMessages(requestId, entity, notificationReceiver));
    }

    @Override
    protected NotificationReceiver enrichEntity(UUID requestId,
                                                NotificationReceiver entity,
                                                NotificationReceiverDto dto,
                                                CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.CREATE) {
            Notification presentedNotificationByDto = notificationService.getPresentedNotificationByDto(requestId, dto.getNotification());
            entity.setNotification(presentedNotificationByDto);
            notificationMessageRepository.findByNotificationId(presentedNotificationByDto.getId())
                    .forEach(notificationMessage -> enrichByMessages(requestId, notificationMessage, entity));
        }
        if (crudOperation == CrudOperation.UPDATE) {
            Notification presentedNotificationByDto = notificationService.getPresentedNotificationByDto(requestId, dto.getNotification());
            entity.setNotification(presentedNotificationByDto);
        }
        if (crudOperation == CrudOperation.DELETE) {
            notificationReceiverMessageService.getNotificationReceiverMessageIdsByReceiverId(entity.getUserId()).stream()
                    .map(NotificationReceiverMessage::getId)
                    .forEach(notificationReceiverMessageId -> notificationReceiverMessageService.delete(requestId, notificationReceiverMessageId));
        }
        return entity;
    }

    @Override
    public String getServiceName() {
        return NOTIFICATION_RECEIVER;
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return Map.of("id", FieldMapper.builder()
                .currentServiceField("userId")
                .fieldClass(UUID.class).build());
    }
}
