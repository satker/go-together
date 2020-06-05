package org.go.together.validation;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.EventPhotoDto;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EventPhotoValidator extends Validator<EventPhotoDto> {
    private final PhotoValidator photoValidator;

    public EventPhotoValidator(PhotoValidator photoValidator) {
        this.photoValidator = photoValidator;
    }

    @Override
    public void getMapsForCheck(EventPhotoDto dto) {
        super.OBJECT_NULL_CHECK = ImmutableMap.<String, Optional<Object>>builder()
                .put("event id", Optional.ofNullable(dto.getEventId()))
                .build();
    }

    @Override
    protected String commonValidateCustom(EventPhotoDto dto) {
        StringBuilder errors = new StringBuilder();

        dto.getPhotos().stream()
                .map(photoValidator::validate)
                .filter(StringUtils::isNotBlank)
                .forEach(errors::append);

        return errors.toString();
    }
}
