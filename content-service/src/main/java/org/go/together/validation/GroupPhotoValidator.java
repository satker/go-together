package org.go.together.validation;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.GroupPhotoDto;
import org.go.together.dto.PhotoDto;
import org.go.together.enums.CrudOperation;
import org.go.together.validation.impl.CommonValidator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupPhotoValidator extends CommonValidator<GroupPhotoDto> {
    private final Validator<PhotoDto> photoValidator;

    @Override
    protected String commonValidation(GroupPhotoDto dto, CrudOperation crudOperation) {
        StringBuilder errors = new StringBuilder();

        dto.getPhotos().stream()
                .map(photoDto -> photoValidator.validate(photoDto, crudOperation))
                .filter(StringUtils::isNotBlank)
                .forEach(errors::append);

        return errors.toString();
    }
}
