package org.go.together.test.validation;

import org.go.together.test.dto.ManyJoinDto;
import org.go.together.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ManyJoinValidator extends Validator<ManyJoinDto> {
    @Override
    public void getMapsForCheck(ManyJoinDto dto) {
        super.STRINGS_FOR_BLANK_CHECK = Map.of("many join name", ManyJoinDto::getName);
        super.NUMBER_CORRECT_ZERO_OR_NEGATIVE_CHECK = Map.of("many join number", ManyJoinDto::getNumber);
    }
}
