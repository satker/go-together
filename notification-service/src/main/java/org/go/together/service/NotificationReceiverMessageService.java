package org.go.together.service;

import org.go.together.CrudServiceImpl;
import org.go.together.dto.FieldMapper;
import org.go.together.dto.NotificationReceiverMessageDto;
import org.go.together.mapper.NotificationReceiverMessageMapper;
import org.go.together.model.NotificationReceiverMessage;
import org.go.together.repository.NotificationReceiverMessageRepository;
import org.go.together.validation.NotificationReceiverMessageValidator;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationReceiverMessageService extends CrudServiceImpl<NotificationReceiverMessageDto, NotificationReceiverMessage> {

    protected NotificationReceiverMessageService(NotificationReceiverMessageRepository notificationReceiverMessageRepository,
                                                 NotificationReceiverMessageMapper notificationReceiverMessageMapper,
                                                 NotificationReceiverMessageValidator notificationReceiverMessageValidator) {
        super(notificationReceiverMessageRepository, notificationReceiverMessageMapper, notificationReceiverMessageValidator);
    }

    @Override
    public String getServiceName() {
        return "notificationReceiverMessage";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return null;
    }
}
