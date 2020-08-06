package org.go.together.correction;

import org.go.together.dto.NotificationMessageDto;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

@Component
public class NotificationMessageValidator extends Validator<NotificationMessageDto> {
    @Override
    public void getMapsForCheck(NotificationMessageDto dto) {

    }
}
