package org.go.together.service;

import org.go.together.CrudServiceImpl;
import org.go.together.dto.FieldMapper;
import org.go.together.dto.NotificationReceiverMessageDto;
import org.go.together.model.NotificationReceiverMessage;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationReceiverMessageService extends CrudServiceImpl<NotificationReceiverMessageDto, NotificationReceiverMessage> {
    @Override
    public String getServiceName() {
        return "notificationReceiverMessage";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return null;
    }
}
