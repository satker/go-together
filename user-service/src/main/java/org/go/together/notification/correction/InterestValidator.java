package org.go.together.notification.correction;

import org.go.together.notification.dto.InterestDto;
import org.go.together.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class InterestValidator extends Validator<InterestDto> {
    @Override
    public void getMapsForCheck(InterestDto dto) {

    }
}
