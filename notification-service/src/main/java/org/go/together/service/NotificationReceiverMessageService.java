package org.go.together.service;

import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.dto.NotificationReceiverMessageDto;
import org.go.together.mapper.NotificationMessageMapper;
import org.go.together.model.NotificationReceiverMessage;
import org.go.together.repository.NotificationReceiverMessageRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationReceiverMessageService extends CrudServiceImpl<NotificationReceiverMessageDto, NotificationReceiverMessage> {
    private final NotificationMessageMapper notificationMessageMapper;

    public NotificationReceiverMessageService(NotificationMessageMapper notificationMessageMapper) {
        this.notificationMessageMapper = notificationMessageMapper;
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

    public Collection<NotificationMessageDto> getReceiverNotifications(UUID receiverId) {
        return getNotificationReceiverMessageIdsByReceiverId(receiverId)
                .stream()
                .map(notificationReceiverMessage -> {
                    NotificationMessageDto notificationMessageDto =
                            notificationMessageMapper.entityToDto(notificationReceiverMessage.getNotificationMessage());
                    notificationMessageDto.setIsRead(notificationReceiverMessage.getIsRead());
                    return notificationMessageDto;
                }).sorted(Comparator.comparing(NotificationMessageDto::getDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    public Collection<NotificationReceiverMessage> getNotificationReceiverMessageIdsByReceiverId(UUID receiverId) {
        return ((NotificationReceiverMessageRepository) repository).findByReceiverId(receiverId);
    }
}
