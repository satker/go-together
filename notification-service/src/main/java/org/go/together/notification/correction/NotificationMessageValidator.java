package org.go.together.notification.correction;

import org.go.together.notification.dto.NotificationMessageDto;
import org.go.together.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class NotificationMessageValidator extends Validator<NotificationMessageDto> {
    @Override
    public void getMapsForCheck(NotificationMessageDto dto) {

    }
}
