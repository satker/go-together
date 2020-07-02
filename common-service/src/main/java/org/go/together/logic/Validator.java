package org.go.together.logic;

import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.SimpleDto;
import org.go.together.dto.validation.DateIntervalDto;
import org.go.together.dto.validation.NumberIntervalDto;
import org.go.together.dto.validation.StringRegexDto;
import org.go.together.enums.CrudOperation;
import org.go.together.interfaces.ComparableDto;
import org.go.together.utils.ValidatorUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class Validator<CD extends ComparableDto> {
    public Map<String, String> STRINGS_FOR_BLANK_CHECK = new HashMap<>();
    public Map<String, Collection<SimpleDto>> SIMPLE_DTO_CORRECT_CHECK = new HashMap<>();
    public Map<String, Number> NUMBER_CORRECT_ZERO_OR_NEGATIVE_CHECK = new HashMap<>();
    public Map<String, Collection<?>> COLLECTION_CORRECT_CHECK = new HashMap<>();
    public Map<String, StringRegexDto> REGEX_STRING_CORRECT_CHECK = new HashMap<>();
    public Map<String, DateIntervalDto> DATES_CORRECT_CHECK = new HashMap<>();
    public Map<String, Optional<Object>> OBJECT_NULL_CHECK = new HashMap<>();
    public Map<String, NumberIntervalDto> NUMBER_INTERVAL_CORRECT_CHECK = new HashMap<>();

    public String validate(CD dto, CrudOperation crudOperation) {
        getMapsForCheck(dto);
        StringBuilder errors = new StringBuilder();

        errors.append(ValidatorUtils.checkEmptyString(STRINGS_FOR_BLANK_CHECK))
                .append(ValidatorUtils.checkEmptySimpleDto(SIMPLE_DTO_CORRECT_CHECK))
                .append(ValidatorUtils.checkZeroOrNegativeNumber(NUMBER_CORRECT_ZERO_OR_NEGATIVE_CHECK))
                .append(ValidatorUtils.checkCorrectCollection(COLLECTION_CORRECT_CHECK))
                .append(ValidatorUtils.checkStringByRegex(REGEX_STRING_CORRECT_CHECK))
                .append(ValidatorUtils.checkDateIntervalIsCorrect(DATES_CORRECT_CHECK))
                .append(ValidatorUtils.checkNullObject(OBJECT_NULL_CHECK))
                .append(ValidatorUtils.checkNumberIntervalIsCorrect(NUMBER_INTERVAL_CORRECT_CHECK));
        if (StringUtils.isBlank(errors)) {
            errors.append(commonValidation(dto, crudOperation));
        }
        return errors.toString();
    }

    public String validateDtos(Collection<CD> dtos, CrudOperation crudOperation) {
        StringBuilder errors = new StringBuilder();

        dtos.stream()
                .map(dto -> validate(dto, crudOperation))
                .forEach(errors::append);

        return errors.toString();
    }

    public abstract void getMapsForCheck(CD dto);

    protected String commonValidation(CD dto, CrudOperation crudOperation) {
        return StringUtils.EMPTY;
    }
}
