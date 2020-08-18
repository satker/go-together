package org.go.together.validation;

import com.google.common.collect.ImmutableMap;
import org.go.together.dto.PaidThingDto;
import org.springframework.stereotype.Component;

@Component
public class PaidThingValidator extends Validator<PaidThingDto> {
    @Override
    public void getMapsForCheck(PaidThingDto dto) {
        super.STRINGS_FOR_BLANK_CHECK = ImmutableMap.<String, String>builder()
                .put("paid thing name", dto.getName())
                .build();
    }
}
