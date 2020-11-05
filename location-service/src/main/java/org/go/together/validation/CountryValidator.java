package org.go.together.validation;

import org.go.together.dto.CountryDto;
import org.go.together.validation.impl.CommonValidator;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Component
public class CountryValidator extends CommonValidator<CountryDto> {
    @Override
    public Map<String, Function<CountryDto, ?>> getMapsForCheck() {
        return Map.of("country name", CountryDto::getName);
    }
}
