package org.go.together.validation;

import org.go.together.dto.NotificationReceiverDto;
import org.springframework.stereotype.Component;

@Component
public class NotificationReceiverValidator extends Validator<NotificationReceiverDto> {
    @Override
    public void getMapsForCheck(NotificationReceiverDto dto) {

    }
}
