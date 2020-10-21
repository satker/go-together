package org.go.together.validation;

import org.go.together.dto.NotificationReceiverMessageDto;
import org.go.together.validation.impl.CommonValidator;
import org.springframework.stereotype.Component;

@Component
public class NotificationReceiverMessageValidator extends CommonValidator<NotificationReceiverMessageDto> {
    @Override
    public void getMapsForCheck(NotificationReceiverMessageDto dto) {

    }
}
