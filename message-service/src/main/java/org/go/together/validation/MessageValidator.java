package org.go.together.validation;

import com.google.common.collect.ImmutableMap;
import org.go.together.dto.MessageDto;
import org.go.together.dto.validation.NumberIntervalDto;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

@Component
public class MessageValidator extends Validator<MessageDto> {
    @Override
    public void getMapsForCheck(MessageDto dto) {
        super.NUMBER_INTERVAL_CORRECT_CHECK = ImmutableMap.<String, NumberIntervalDto>builder()
                .put("test number interval", new NumberIntervalDto(dto.getRating(), 0, 5))
                .build();
    }
}
