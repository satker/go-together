package org.go.together.validation;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.LocationDto;
import org.go.together.enums.CrudOperation;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

@Component
public class LocationValidator extends Validator<LocationDto> {
    private final CountryValidator countryValidator;

    public LocationValidator(CountryValidator countryValidator) {
        this.countryValidator = countryValidator;
    }

    @Override
    public void getMapsForCheck(LocationDto dto) {
        super.STRINGS_FOR_BLANK_CHECK = ImmutableMap.<String, String>builder()
                .put("location code", dto.getName())
                //.put("location state", dto.getState())
                .build();
    }

    @Override
    protected String commonValidation(LocationDto dto, CrudOperation crudOperation) {
        StringBuilder errors = new StringBuilder();
        String validateCountry = countryValidator.validate(dto.getCountry(), crudOperation);
        if (StringUtils.isNotBlank(validateCountry)) {
            errors.append(validateCountry);
        }
        return errors.toString();
    }
}
