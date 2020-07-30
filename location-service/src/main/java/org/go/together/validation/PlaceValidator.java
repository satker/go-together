package org.go.together.validation;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.PlaceDto;
import org.go.together.enums.CrudOperation;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

@Component
public class PlaceValidator extends Validator<PlaceDto> {
    private final CountryValidator countryValidator;

    public PlaceValidator(CountryValidator countryValidator) {
        this.countryValidator = countryValidator;
    }

    @Override
    public void getMapsForCheck(PlaceDto dto) {
        super.STRINGS_FOR_BLANK_CHECK = ImmutableMap.<String, String>builder()
                .put("location code", dto.getName())
                .build();
    }

    @Override
    protected String commonValidation(PlaceDto dto, CrudOperation crudOperation) {
        StringBuilder errors = new StringBuilder();
        String validateCountry = countryValidator.validate(dto.getCountry(), crudOperation);
        if (StringUtils.isNotBlank(validateCountry)) {
            errors.append(validateCountry);
        }
        return errors.toString();
    }
}
