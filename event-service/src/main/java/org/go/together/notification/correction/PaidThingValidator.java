package org.go.together.notification.correction;

import com.google.common.collect.ImmutableMap;
import org.go.together.notification.dto.PaidThingDto;
import org.go.together.validation.Validator;
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
