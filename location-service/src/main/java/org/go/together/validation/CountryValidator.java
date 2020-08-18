package org.go.together.validation;

import com.google.common.collect.ImmutableMap;
import org.go.together.dto.CountryDto;
import org.springframework.stereotype.Component;

@Component
public class CountryValidator extends Validator<CountryDto> {
    @Override
    public void getMapsForCheck(CountryDto dto) {
        super.STRINGS_FOR_BLANK_CHECK = ImmutableMap.<String, String>builder()
                .put("country name", dto.getName())
                .build();
    }
}
