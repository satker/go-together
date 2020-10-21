package org.go.together.test.validation;

import org.go.together.test.dto.JoinTestDto;
import org.go.together.validation.impl.CommonValidator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JoinTestValidator extends CommonValidator<JoinTestDto> {
    @Override
    public void getMapsForCheck(JoinTestDto dto) {
        super.STRINGS_FOR_BLANK_CHECK = Map.of("join test name", JoinTestDto::getName);
    }
}
