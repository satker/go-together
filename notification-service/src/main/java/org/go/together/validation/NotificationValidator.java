package org.go.together.validation;

import org.go.together.dto.NotificationDto;
import org.springframework.stereotype.Component;

@Component
public class NotificationValidator extends Validator<NotificationDto> {
    @Override
    public void getMapsForCheck(NotificationDto dto) {

    }
}
