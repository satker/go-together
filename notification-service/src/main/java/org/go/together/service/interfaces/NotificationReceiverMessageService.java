package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.dto.NotificationReceiverMessageDto;
import org.go.together.find.FindService;
import org.go.together.model.NotificationReceiverMessage;

import java.util.Collection;
import java.util.UUID;

public interface NotificationReceiverMessageService extends CrudService<NotificationReceiverMessageDto>,
        FindService<NotificationReceiverMessage> {
    boolean readNotifications(UUID receiverId);

    Collection<NotificationReceiverMessage> getNotificationReceiverMessageIdsByReceiverId(UUID receiverId);
}
