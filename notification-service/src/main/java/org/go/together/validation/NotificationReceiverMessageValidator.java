package org.go.together.validation;

import org.go.together.dto.NotificationReceiverMessageDto;
import org.springframework.stereotype.Component;

@Component
public class NotificationReceiverMessageValidator extends Validator<NotificationReceiverMessageDto> {
    @Override
    public void getMapsForCheck(NotificationReceiverMessageDto dto) {

    }
}
