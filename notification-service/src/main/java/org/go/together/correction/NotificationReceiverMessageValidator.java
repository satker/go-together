package org.go.together.correction;

import org.go.together.dto.NotificationReceiverMessageDto;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

@Component
public class NotificationReceiverMessageValidator extends Validator<NotificationReceiverMessageDto> {
    @Override
    public void getMapsForCheck(NotificationReceiverMessageDto dto) {

    }
}
