package org.go.together.service;

import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.dto.IdDto;
import org.go.together.enums.NotificationStatus;
import org.go.together.find.dto.FieldMapper;
import org.go.together.mapper.NotificationMessageMapper;
import org.go.together.mapper.NotificationReceiverMapper;
import org.go.together.model.NotificationMessage;
import org.go.together.model.NotificationReceiver;
import org.go.together.notification.dto.NotificationDto;
import org.go.together.notification.dto.NotificationMessageDto;
import org.go.together.notification.dto.NotificationReceiverMessageDto;
import org.go.together.notification.repository.NotificationReceiverRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationMessageService extends CrudServiceImpl<NotificationMessageDto, NotificationMessage> {
    private final NotificationMessageMapper notificationMessageMapper;
    private final NotificationService notificationService;
    private final NotificationReceiverRepository notificationReceiverRepository;
    private final NotificationReceiverMapper notificationReceiverMapper;
    private final NotificationReceiverService notificationReceiverService;
    private final NotificationReceiverMessageService notificationReceiverMessageService;

    public NotificationMessageService(NotificationMessageMapper notificationMessageMapper,
                                      NotificationService notificationService,
                                      NotificationReceiverRepository notificationReceiverRepository,
                                      NotificationReceiverMapper notificationReceiverMapper,
                                      NotificationReceiverService notificationReceiverService,
                                      NotificationReceiverMessageService notificationReceiverMessageService) {
        this.notificationMessageMapper = notificationMessageMapper;
        this.notificationService = notificationService;
        this.notificationReceiverRepository = notificationReceiverRepository;
        this.notificationReceiverMapper = notificationReceiverMapper;
        this.notificationReceiverService = notificationReceiverService;
        this.notificationReceiverMessageService = notificationReceiverMessageService;
    }

    public Collection<NotificationMessageDto> getReceiverNotifications(UUID receiverId) {
        return notificationReceiverRepository.findByReceiverId(receiverId)
                .stream()
                .map(NotificationReceiver::getNotificationReceiverMessages)
                .flatMap(Collection::stream)
                .map(notificationReceiverMessage -> {
                    NotificationMessageDto notificationMessageDto =
                            notificationMessageMapper.entityToDto(notificationReceiverMessage.getNotificationMessage());
                    notificationMessageDto.setIsRead(notificationReceiverMessage.getIsRead());
                    return notificationMessageDto;
                }).sorted(Comparator.comparing(NotificationMessageDto::getDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }


    public boolean notificate(UUID producerId, NotificationStatus status, NotificationMessageDto notificationMessage) {
        NotificationDto savedNotificationDto = notificationService.saveNotificationByProducerId(producerId, status);
        saveNotificationMessage(notificationMessage, savedNotificationDto);
        return true;
    }

    private void saveNotificationMessage(NotificationMessageDto notificationMessage, NotificationDto notificationDto) {
        notificationMessage.setNotification(notificationDto);
        IdDto saveNotificationMessageId = super.create(notificationMessage);

        Collection<NotificationReceiver> notificationReceivers =
                notificationReceiverRepository.findByNotificationId(notificationDto.getId());
        notificationReceivers.stream()
                .map(notificationReceiverMapper::entityToDto)
                .peek(notificationReceiverDto -> {
                    Set<NotificationReceiverMessageDto> notificationReceiverMessages = notificationReceiverDto.getNotificationReceiverMessages();

                    NotificationReceiverMessageDto notificationReceiverMessageDto = new NotificationReceiverMessageDto();
                    notificationReceiverMessageDto.setIsRead(false);
                    notificationReceiverMessageDto.setNotificationMessage(super.read(saveNotificationMessageId.getId()));
                    IdDto notificationReceiverMessageId = notificationReceiverMessageService.create(notificationReceiverMessageDto);

                    notificationReceiverMessages.add(notificationReceiverMessageService.read(notificationReceiverMessageId.getId()));
                    notificationReceiverDto.setNotificationReceiverMessages(notificationReceiverMessages);
                })
                .forEach(notificationReceiverService::create);
    }

    @Override
    public String getServiceName() {
        return "notificationMessage";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return null;
    }
}
