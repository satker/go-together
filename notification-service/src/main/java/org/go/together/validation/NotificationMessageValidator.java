package org.go.together.validation;

import org.go.together.dto.NotificationMessageDto;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class NotificationMessageValidator extends CommonValidator<NotificationMessageDto> {
    @Override
    public Map<String, Function<NotificationMessageDto, ?>> getMapsForCheck(UUID requestId) {
        return Map.of("message", NotificationMessageDto::getMessage);
    }
}
