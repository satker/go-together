package org.go.together.test.validation;

import com.google.common.collect.ImmutableMap;
import org.go.together.test.dto.JoinTestDto;
import org.go.together.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class JoinTestValidator extends Validator<JoinTestDto> {
    @Override
    public void getMapsForCheck(JoinTestDto dto) {
        super.STRINGS_FOR_BLANK_CHECK = ImmutableMap.<String, String>builder()
                .put("join test name", dto.getName())
                .build();
    }
}
