package org.go.together.test.validation;

import com.google.common.collect.ImmutableMap;
import org.go.together.logic.Validator;
import org.go.together.test.dto.ManyJoinDto;
import org.springframework.stereotype.Component;

@Component
public class ManyJoinValidator extends Validator<ManyJoinDto> {
    @Override
    public void getMapsForCheck(ManyJoinDto dto) {
        super.STRINGS_FOR_BLANK_CHECK = ImmutableMap.<String, String>builder()
                .put("many join name", dto.getName())
                .build();
        super.NUMBER_CORRECT_ZERO_OR_NEGATIVE_CHECK = ImmutableMap.<String, Number>builder()
                .put("many join number", dto.getNumber())
                .build();
    }
}
