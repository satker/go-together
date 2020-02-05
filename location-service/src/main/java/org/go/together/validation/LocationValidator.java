package org.go.together.validation;

import org.go.together.dto.LocationDto;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

@Component
public class LocationValidator extends Validator<LocationDto> {

    @Override
    public void getMapsForCheck(LocationDto dto) {

    }
}
