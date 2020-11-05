package org.go.together.validation;

import org.go.together.dto.MessageDto;
import org.go.together.validation.dto.NumberIntervalDto;
import org.go.together.validation.impl.CommonValidator;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
public class MessageValidator extends CommonValidator<MessageDto> {
    @Override
    public Map<String, Function<MessageDto, ?>> getMapsForCheck() {
        return Map.of(
                "message", MessageDto::getMessage,
                "test number interval", messageDto -> new NumberIntervalDto(Optional.ofNullable(messageDto.getRating()).orElse(0D),
                        0, 5));
    }
}
