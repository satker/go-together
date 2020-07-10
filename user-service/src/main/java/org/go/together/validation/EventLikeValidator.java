package org.go.together.validation;

import org.go.together.dto.EventLikeDto;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

@Component
public class EventLikeValidator extends Validator<EventLikeDto> {
    @Override
    public void getMapsForCheck(EventLikeDto dto) {

    }
}
