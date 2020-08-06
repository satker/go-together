package org.go.together.correction;

import org.go.together.dto.NotificationReceiverDto;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

@Component
public class NotificationReceiverValidator extends Validator<NotificationReceiverDto> {
    @Override
    public void getMapsForCheck(NotificationReceiverDto dto) {

    }
}
