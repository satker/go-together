package org.go.together.service;

import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.dto.NotificationReceiverMessageDto;
import org.go.together.find.dto.FieldMapper;
import org.go.together.model.NotificationReceiverMessage;
import org.go.together.repository.interfaces.NotificationReceiverMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Service
public class NotificationReceiverMessageService extends CrudServiceImpl<NotificationReceiverMessageDto, NotificationReceiverMessage> {
    private NotificationReceiverService notificationReceiverService;
    private NotificationMessageService notificationMessageService;

    @Autowired
    public void setNotificationReceiverService(NotificationReceiverService notificationReceiverService) {
        this.notificationReceiverService = notificationReceiverService;
    }

    @Autowired
    public void setNotificationMessageService(NotificationMessageService notificationMessageService) {
        this.notificationMessageService = notificationMessageService;
    }

    @Override
    public String getServiceName() {
        return "notificationReceiverMessage";
    }

    public boolean readNotifications(UUID receiverId) {
        getNotificationReceiverMessageIdsByReceiverId(receiverId).stream()
                .map(mapper::entityToDto)
                .peek(notificationReceiverMessage -> notificationReceiverMessage.setIsRead(true))
                .forEach(super::update);
        return true;
    }

    public Collection<NotificationReceiverMessage> getNotificationReceiverMessageIdsByReceiverId(UUID receiverId) {
        return ((NotificationReceiverMessageRepository) repository).findByReceiverId(receiverId);
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return Map.of("receiver", FieldMapper.builder()
                        .currentServiceField("notificationReceiver")
                        .innerService(notificationReceiverService).build(),
                "message", FieldMapper.builder()
                        .currentServiceField("notificationMessage")
                        .innerService(notificationMessageService).build(),
                "isRead", FieldMapper.builder()
                        .currentServiceField("isRead").build());
    }
}
