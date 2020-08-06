package org.go.together.correction;

import org.go.together.dto.InterestDto;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

@Component
public class InterestValidator extends Validator<InterestDto> {
    @Override
    public void getMapsForCheck(InterestDto dto) {

    }
}
