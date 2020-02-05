package org.go.together.validation;

import org.go.together.dto.EventPhotoDto;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

@Component
public class EventPhotoValidator extends Validator<EventPhotoDto> {
    @Override
    public void getMapsForCheck(EventPhotoDto dto) {

    }
}
