package org.go.together.correction;

import org.go.together.dto.EventPaidThingDto;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

@Component
public class EventPaidThingValidator extends Validator<EventPaidThingDto> {
    @Override
    public void getMapsForCheck(EventPaidThingDto dto) {

    }
}
