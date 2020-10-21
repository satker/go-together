package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.dto.NotificationReceiverDto;
import org.go.together.find.FindService;
import org.go.together.model.NotificationMessage;
import org.go.together.model.NotificationReceiver;

import java.util.UUID;

public interface NotificationReceiverService extends CrudService<NotificationReceiverDto>,
        FindService<NotificationReceiver> {
    void addReceiver(UUID producerId, UUID receiverId);

    void removeReceiver(UUID producerId, UUID receiverId);

    void notificateMessageReceivers(NotificationMessage entity, NotificationMessageDto dto);
}
