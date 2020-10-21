package org.go.together.service.impl;

import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.enums.CrudOperation;
import org.go.together.find.dto.FieldMapper;
import org.go.together.model.NotificationMessage;
import org.go.together.service.interfaces.NotificationMessageService;
import org.go.together.service.interfaces.NotificationReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class NotificationMessageServiceImpl extends CrudServiceImpl<NotificationMessageDto, NotificationMessage>
        implements NotificationMessageService {
    private NotificationReceiverService notificationReceiverService;

    @Autowired
    public void setNotificationReceiverService(NotificationReceiverService notificationReceiverService) {
        this.notificationReceiverService = notificationReceiverService;
    }

    @Override
    protected NotificationMessage enrichEntity(NotificationMessage entity,
                                               NotificationMessageDto dto,
                                               CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.CREATE) {
            notificationReceiverService.notificateMessageReceivers(entity, dto);
        }
        return entity;
    }

    @Override
    public String getServiceName() {
        return "notificationMessage";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return Map.of("date", FieldMapper.builder()
                .currentServiceField("date")
                .fieldClass(Date.class).build());
    }
}
