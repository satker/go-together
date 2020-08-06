package org.go.together.notification.correction;

import com.google.common.collect.ImmutableMap;
import org.go.together.notification.dto.CountryDto;
import org.go.together.validation.Validator;
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
