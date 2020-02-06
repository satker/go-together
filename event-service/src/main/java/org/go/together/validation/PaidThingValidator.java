package org.go.together.validation;

import org.go.together.dto.PaidThingDto;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

@Component
public class PaidThingValidator extends Validator<PaidThingDto> {
    @Override
    public void getMapsForCheck(PaidThingDto dto) {

    }
}
