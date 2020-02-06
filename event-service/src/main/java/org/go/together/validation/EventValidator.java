package org.go.together.validation;

import org.go.together.dto.EventDto;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

@Component
public class EventValidator extends Validator<EventDto> {
    @Override
    public void getMapsForCheck(EventDto dto) {

    }
}
