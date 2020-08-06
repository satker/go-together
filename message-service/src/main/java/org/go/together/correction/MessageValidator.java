package org.go.together.correction;

import com.google.common.collect.ImmutableMap;
import org.go.together.dto.MessageDto;
import org.go.together.dto.validation.NumberIntervalDto;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MessageValidator extends Validator<MessageDto> {
    @Override
    public void getMapsForCheck(MessageDto dto) {
        super.NUMBER_INTERVAL_CORRECT_CHECK = ImmutableMap.<String, NumberIntervalDto>builder()
                .put("test number interval", new NumberIntervalDto(Optional.ofNullable(dto.getRating()).orElse(0D),
                        0, 5))
                .build();
    }
}
