package org.go.together.validation;

import org.go.together.dto.NotificationMessageDto;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

@Component
public class NotificationMessageValidator extends Validator<NotificationMessageDto> {
    @Override
    public void getMapsForCheck(NotificationMessageDto dto) {

    }
}
