package org.go.together.notification.correction;

import org.go.together.notification.dto.NotificationReceiverDto;
import org.go.together.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class NotificationReceiverValidator extends Validator<NotificationReceiverDto> {
    @Override
    public void getMapsForCheck(NotificationReceiverDto dto) {

    }
}
