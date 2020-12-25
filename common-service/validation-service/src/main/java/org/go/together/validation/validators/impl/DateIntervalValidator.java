package org.go.together.validation.validators.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.validation.dto.DateIntervalDto;
import org.go.together.validation.validators.interfaces.ObjectValidator;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DateIntervalValidator implements ObjectValidator<DateIntervalDto> {
    public static final String MESSAGE = "Date interval for %s is incorrect. ";

    @Override
    public String checkDto(String fieldName, DateIntervalDto result) {
        if (result.getStartDate() == null || result.getEndDate() == null) {
            return "Dates is not entered. ";
        }

        Date currentDate = new Date();
        if ((!(currentDate.before(result.getStartDate()) &&
                currentDate.before(result.getEndDate()))) ||
                result.getStartDate().after(result.getEndDate())) {
            return String.format(MESSAGE, fieldName);
        } else {
            return StringUtils.EMPTY;
        }
    }
}
