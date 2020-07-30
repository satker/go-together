package org.go.together.validation;

import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.GroupPhotoDto;
import org.go.together.enums.CrudOperation;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

@Component
public class GroupPhotoValidator extends Validator<GroupPhotoDto> {
    private final PhotoValidator photoValidator;

    public GroupPhotoValidator(PhotoValidator photoValidator) {
        this.photoValidator = photoValidator;
    }

    @Override
    public void getMapsForCheck(GroupPhotoDto dto) {
    }

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
