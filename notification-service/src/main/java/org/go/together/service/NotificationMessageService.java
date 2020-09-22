package org.go.together.service;

import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.dto.IdDto;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.dto.NotificationReceiverDto;
import org.go.together.dto.NotificationReceiverMessageDto;
import org.go.together.enums.CrudOperation;
import org.go.together.mapper.NotificationReceiverMapper;
import org.go.together.model.NotificationMessage;
import org.go.together.repository.NotificationReceiverRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class NotificationMessageService extends CrudServiceImpl<NotificationMessageDto, NotificationMessage> {
    private final NotificationReceiverRepository notificationReceiverRepository;
    private final NotificationReceiverMapper notificationReceiverMapper;
    private final NotificationReceiverService notificationReceiverService;
    private final NotificationReceiverMessageService notificationReceiverMessageService;

    public NotificationMessageService(NotificationReceiverRepository notificationReceiverRepository,
                                      NotificationReceiverMapper notificationReceiverMapper,
                                      NotificationReceiverService notificationReceiverService,
                                      NotificationReceiverMessageService notificationReceiverMessageService) {
        this.notificationReceiverRepository = notificationReceiverRepository;
        this.notificationReceiverMapper = notificationReceiverMapper;
        this.notificationReceiverService = notificationReceiverService;
        this.notificationReceiverMessageService = notificationReceiverMessageService;
    }

    @Override
    protected NotificationMessage enrichEntity(NotificationMessage entity,
                                               NotificationMessageDto dto,
                                               CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.CREATE) {
            notificationReceiverRepository.findByNotificationId(dto.getNotificationId()).stream()
                    .map(notificationReceiverMapper::entityToDto)
                    .peek(notificationReceiverDto -> notificateReceivers(entity, notificationReceiverDto))
                    .forEach(notificationReceiverService::update);
        }
        return entity;
    }

    private void notificateReceivers(NotificationMessage entity, NotificationReceiverDto notificationReceiverDto) {
        Set<NotificationReceiverMessageDto> notificationReceiverMessages = notificationReceiverDto.getNotificationReceiverMessages();

        NotificationReceiverMessageDto notificationReceiverMessageDto = new NotificationReceiverMessageDto();
        notificationReceiverMessageDto.setIsRead(false);
        NotificationMessageDto notificationMessage = super.read(entity.getId());
        notificationReceiverMessageDto.setNotificationMessage(notificationMessage);
        IdDto notificationReceiverMessageId = notificationReceiverMessageService.create(notificationReceiverMessageDto);

        NotificationReceiverMessageDto receiverMessageDto =
                notificationReceiverMessageService.read(notificationReceiverMessageId.getId());
        notificationReceiverMessages.add(receiverMessageDto);
        notificationReceiverDto.setNotificationReceiverMessages(notificationReceiverMessages);
    }

    @Override
    public String getServiceName() {
        return "notificationMessage";
    }
}
