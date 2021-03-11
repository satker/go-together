package org.go.together.service.impl;

import org.go.together.base.CommonCrudService;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.enums.CrudOperation;
import org.go.together.model.Notification;
import org.go.together.model.NotificationMessage;
import org.go.together.service.interfaces.NotificationMessageService;
import org.go.together.service.interfaces.NotificationReceiverService;
import org.go.together.service.interfaces.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

import static org.go.together.enums.NotificationServiceInfo.NOTIFICATION_MESSAGE;

@Service
public class NotificationMessageServiceImpl extends CommonCrudService<NotificationMessageDto, NotificationMessage>
        implements NotificationMessageService {
    private final NotificationService notificationService;

    private NotificationReceiverService notificationReceiverService;

    public NotificationMessageServiceImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Autowired
    public void setNotificationReceiverService(NotificationReceiverService notificationReceiverService) {
        this.notificationReceiverService = notificationReceiverService;
    }

    @Override
    protected NotificationMessage enrichEntity(NotificationMessage entity,
                                               NotificationMessageDto dto,
                                               CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.CREATE) {
            Notification notificationDto = notificationService.getPresentedNotificationByDto(dto.getNotification());
            entity.setNotification(notificationDto);
            notificationReceiverService.notificateMessageReceivers(entity);
        } else if (crudOperation == CrudOperation.UPDATE) {
            Notification notificationDto = notificationService.getPresentedNotificationByDto(dto.getNotification());
            entity.setNotification(notificationDto);
            notificationReceiverService.notificateMessageReceivers(entity);
        }
        return entity;
    }

    @Override
    public String getServiceName() {
        return NOTIFICATION_MESSAGE;
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return Map.of("date", FieldMapper.builder()
                .currentServiceField("date")
                .fieldClass(Date.class).build());
    }
}
