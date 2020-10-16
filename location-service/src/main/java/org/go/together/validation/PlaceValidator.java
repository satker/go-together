package org.go.together.validation;

import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.PlaceDto;
import org.go.together.enums.CrudOperation;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PlaceValidator extends Validator<PlaceDto> {
    private final CountryValidator countryValidator;

    public PlaceValidator(CountryValidator countryValidator) {
        this.countryValidator = countryValidator;
    }

    @Override
    public void getMapsForCheck(PlaceDto dto) {
        super.STRINGS_FOR_BLANK_CHECK = Map.of(
                "location code", PlaceDto::getName);
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
