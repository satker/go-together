package org.go.together.notification.correction;

import org.go.together.notification.dto.NotificationDto;
import org.go.together.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class NotificationValidator extends Validator<NotificationDto> {
    @Override
    public void getMapsForCheck(NotificationDto dto) {

    }
}
