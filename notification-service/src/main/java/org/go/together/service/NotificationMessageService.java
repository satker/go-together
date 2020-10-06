package org.go.together.service;

import com.google.common.collect.ImmutableMap;
import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.dto.NotificationReceiverDto;
import org.go.together.dto.NotificationReceiverMessageDto;
import org.go.together.enums.CrudOperation;
import org.go.together.find.dto.FieldMapper;
import org.go.together.mapper.NotificationReceiverMapper;
import org.go.together.model.NotificationMessage;
import org.go.together.repository.NotificationReceiverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class NotificationMessageService extends CrudServiceImpl<NotificationMessageDto, NotificationMessage> {
    private final NotificationReceiverRepository notificationReceiverRepository;
    private final NotificationReceiverMapper notificationReceiverMapper;
    private NotificationReceiverMessageService notificationReceiverMessageService;

    public NotificationMessageService(NotificationReceiverRepository notificationReceiverRepository,
                                      NotificationReceiverMapper notificationReceiverMapper) {
        this.notificationReceiverRepository = notificationReceiverRepository;
        this.notificationReceiverMapper = notificationReceiverMapper;
    }

    @Autowired
    public void setNotificationReceiverMessageService(NotificationReceiverMessageService notificationReceiverMessageService) {
        this.notificationReceiverMessageService = notificationReceiverMessageService;
    }

    @Override
    protected NotificationMessage enrichEntity(NotificationMessage entity,
                                               NotificationMessageDto dto,
                                               CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.CREATE) {
            notificationReceiverRepository.findByNotificationId(dto.getNotificationId()).stream()
                    .map(notificationReceiverMapper::entityToDto)
                    .forEach(notificationReceiverDto -> notificateReceivers(entity, notificationReceiverDto));
        }
        return entity;
    }

    private void notificateReceivers(NotificationMessage entity, NotificationReceiverDto notificationReceiverDto) {
        NotificationReceiverMessageDto notificationReceiverMessageDto = new NotificationReceiverMessageDto();
        notificationReceiverMessageDto.setIsRead(false);
        NotificationMessageDto notificationMessage = super.read(entity.getId());
        notificationReceiverMessageDto.setNotificationMessage(notificationMessage);
        notificationReceiverMessageDto.setNotificationReceiver(notificationReceiverDto);
        notificationReceiverMessageService.create(notificationReceiverMessageDto);
    }

    @Override
    public String getServiceName() {
        return "notificationMessage";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return ImmutableMap.<String, FieldMapper>builder()
                .put("date", FieldMapper.builder()
                        .currentServiceField("date")
                        .fieldClass(Date.class).build())
                .build();
    }
}
