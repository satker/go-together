package org.go.together.validation;

import org.go.together.dto.EventPaidThingDto;
import org.go.together.validation.impl.CommonValidator;
import org.springframework.stereotype.Component;

@Component
public class EventPaidThingValidator extends CommonValidator<EventPaidThingDto> {
    @Override
    public void getMapsForCheck(EventPaidThingDto dto) {

    }
}
