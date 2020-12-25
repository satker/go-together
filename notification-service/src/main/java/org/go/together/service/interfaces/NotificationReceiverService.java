package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.base.FindService;
import org.go.together.dto.NotificationReceiverDto;
import org.go.together.model.NotificationMessage;

import java.util.UUID;

public interface NotificationReceiverService extends CrudService<NotificationReceiverDto>,
        FindService<NotificationReceiverDto> {
    void notificateMessageReceivers(UUID requestId, NotificationMessage entity);
}
