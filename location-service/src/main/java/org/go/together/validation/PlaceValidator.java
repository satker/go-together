package org.go.together.validation;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.go.together.base.Validator;
import org.go.together.dto.CountryDto;
import org.go.together.dto.PlaceDto;
import org.go.together.enums.CrudOperation;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class PlaceValidator extends CommonValidator<PlaceDto> {
    private final Validator<CountryDto> countryValidator;

    @Override
    public Map<String, Function<PlaceDto, ?>> getMapsForCheck(UUID requestId) {
        return Map.of("location code", PlaceDto::getName);
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
