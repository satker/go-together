package org.go.together.validation;

import org.go.together.dto.NotificationDto;
import org.go.together.validation.impl.CommonValidator;
import org.springframework.stereotype.Component;

@Component
public class NotificationValidator extends CommonValidator<NotificationDto> {
    @Override
    public void getMapsForCheck(NotificationDto dto) {

    }
}
