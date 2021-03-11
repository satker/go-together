package org.go.together.test.validation;

import org.go.together.test.dto.ManyJoinDto;
import org.go.together.validation.CommonValidator;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Component
public class ManyJoinValidator extends CommonValidator<ManyJoinDto> {
    @Override
    public Map<String, Function<ManyJoinDto, ?>> getMapsForCheck() {
        return Map.of("many join name", ManyJoinDto::getName,
                "many join number", ManyJoinDto::getNumber);
    }
}
