package org.go.together.correction;

import org.go.together.dto.NotificationDto;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

@Component
public class NotificationValidator extends Validator<NotificationDto> {
    @Override
    public void getMapsForCheck(NotificationDto dto) {

    }
}
