package org.go.together.validation;

import org.go.together.dto.InterestDto;
import org.springframework.stereotype.Component;

@Component
public class InterestValidator extends Validator<InterestDto> {
    @Override
    public void getMapsForCheck(InterestDto dto) {

    }
}
