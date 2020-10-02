package org.go.together.validation;

import org.go.together.dto.MessageDto;
import org.go.together.validation.dto.NumberIntervalDto;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class MessageValidator extends Validator<MessageDto> {
    @Override
    public void getMapsForCheck(MessageDto dto) {
        super.STRINGS_FOR_BLANK_CHECK = Map.of(
                "message", MessageDto::getMessage
        );
        super.NUMBER_INTERVAL_CORRECT_CHECK = Map.of(
                "test number interval", new NumberIntervalDto(Optional.ofNullable(dto.getRating()).orElse(0D),
                        0, 5));
    }
}
