package org.go.together.validation.test.validator;

import org.go.together.validation.CommonValidator;
import org.go.together.validation.test.dto.ManyJoinDto;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class ManyJoinValidator extends CommonValidator<ManyJoinDto> {
    @Override
    public Map<String, Function<ManyJoinDto, ?>> getMapsForCheck(UUID requestId) {
        return Map.of("many join name", ManyJoinDto::getName,
                "many join number", ManyJoinDto::getNumber);
    }
}
