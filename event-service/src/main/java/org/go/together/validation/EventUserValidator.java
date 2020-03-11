package org.go.together.validation;

import org.go.together.dto.EventUserDto;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

@Component
public class EventUserValidator extends Validator<EventUserDto> {
    @Override
    public void getMapsForCheck(EventUserDto dto) {

    }
}
