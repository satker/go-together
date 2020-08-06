package org.go.together.notification.correction;

import org.go.together.notification.dto.NotificationReceiverMessageDto;
import org.go.together.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class NotificationReceiverMessageValidator extends Validator<NotificationReceiverMessageDto> {
    @Override
    public void getMapsForCheck(NotificationReceiverMessageDto dto) {

    }
}
