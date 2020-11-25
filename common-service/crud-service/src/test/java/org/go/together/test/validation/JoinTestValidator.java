package org.go.together.test.validation;

import org.go.together.test.dto.JoinTestDto;
import org.go.together.validation.CommonValidator;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JoinTestValidator extends CommonValidator<JoinTestDto> {
    @Override
    public Map<String, Function<JoinTestDto, ?>> getMapsForCheck(UUID requestId) {
        return Map.of("join test name", JoinTestDto::getName);
    }
}
