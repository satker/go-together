package org.go.together.validation.test.validator;

import org.go.together.validation.CommonValidator;
import org.go.together.validation.test.dto.JoinTestDto;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Component
public class JoinTestValidator extends CommonValidator<JoinTestDto> {
    @Override
    public Map<String, Function<JoinTestDto, ?>> getMapsForCheck() {
        return Map.of("join test name", JoinTestDto::getName);
    }
}
