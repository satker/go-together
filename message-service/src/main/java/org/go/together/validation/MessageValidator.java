package org.go.together.validation;

import org.go.together.dto.MessageDto;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

@Component
public class MessageValidator extends Validator<MessageDto> {
    @Override
    public void getMapsForCheck(MessageDto dto) {

    }
}
