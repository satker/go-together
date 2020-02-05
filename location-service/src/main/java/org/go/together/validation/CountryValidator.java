package org.go.together.validation;

import org.go.together.dto.CountryDto;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

@Component
public class CountryValidator extends Validator<CountryDto> {
    @Override
    public void getMapsForCheck(CountryDto dto) {

    }
}
