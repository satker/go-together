package org.go.together.validation;

import org.go.together.dto.NotificationMessageDto;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificationMessageValidator extends Validator<NotificationMessageDto> {
    @Override
    public void getMapsForCheck(NotificationMessageDto dto) {
        super.STRINGS_FOR_BLANK_CHECK = Map.of("messsage", NotificationMessageDto::getMessage);
    }
}
