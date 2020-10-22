package org.go.together.validation;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.CountryDto;
import org.go.together.dto.PlaceDto;
import org.go.together.enums.CrudOperation;
import org.go.together.validation.impl.CommonValidator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PlaceValidator extends CommonValidator<PlaceDto> {
    private final Validator<CountryDto> countryValidator;

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
