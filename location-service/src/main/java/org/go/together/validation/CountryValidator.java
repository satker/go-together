package org.go.together.validation;

import org.go.together.dto.CountryDto;
import org.go.together.validation.impl.CommonValidator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CountryValidator extends CommonValidator<CountryDto> {
    @Override
    public void getMapsForCheck(CountryDto dto) {
        super.STRINGS_FOR_BLANK_CHECK = Map.of("country name", CountryDto::getName);
    }
}
