package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.find.FindService;
import org.go.together.model.NotificationMessage;

public interface NotificationMessageService extends CrudService<NotificationMessageDto>, FindService<NotificationMessage> {
}
