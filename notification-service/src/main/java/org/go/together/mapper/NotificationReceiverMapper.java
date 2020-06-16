package org.go.together.mapper;

import org.go.together.dto.NotificationReceiverDto;
import org.go.together.interfaces.Mapper;
import org.go.together.model.NotificationReceiver;
import org.springframework.stereotype.Component;

@Component
public class NotificationReceiverMapper implements Mapper<NotificationReceiverDto, NotificationReceiver> {
    @Override
    public NotificationReceiverDto entityToDto(NotificationReceiver entity) {
        NotificationReceiverDto notificationReceiverDto = new NotificationReceiverDto();
        notificationReceiverDto.setId(entity.getId());
        notificationReceiverDto.setUserId(entity.getUserId());
        return notificationReceiverDto;
    }

    @Override
    public NotificationReceiver dtoToEntity(NotificationReceiverDto dto) {
        NotificationReceiver notificationReceiver = new NotificationReceiver();
        notificationReceiver.setId(dto.getId());
        notificationReceiver.setUserId(dto.getUserId());
        notificationReceiver.setIsRead(dto.getIsRead());
        return notificationReceiver;
    }
}
