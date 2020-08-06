package org.go.together.notification.correction;

import org.go.together.notification.dto.EventPaidThingDto;
import org.go.together.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class EventPaidThingValidator extends Validator<EventPaidThingDto> {
    @Override
    public void getMapsForCheck(EventPaidThingDto dto) {

    }
}
