package org.go.together.validation;

import org.go.together.dto.CountryDto;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class CountryValidator extends CommonValidator<CountryDto> {
    @Override
    public Map<String, Function<CountryDto, ?>> getMapsForCheck(UUID requestId) {
        return Map.of("country name", CountryDto::getName);
    }
}
