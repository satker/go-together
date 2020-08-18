package org.go.together.service;

import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.dto.NotificationReceiverMessageDto;
import org.go.together.model.NotificationReceiverMessage;
import org.springframework.stereotype.Service;

@Service
public class NotificationReceiverMessageService extends CrudServiceImpl<NotificationReceiverMessageDto, NotificationReceiverMessage> {
    @Override
    public String getServiceName() {
        return "notificationReceiverMessage";
    }
}
