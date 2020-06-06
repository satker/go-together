package org.go.together.validation;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Strings;
import org.go.together.dto.EventLocationDto;
import org.go.together.logic.Validator;
import org.go.together.mapper.CountryMapper;
import org.go.together.model.Country;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EventLocationValidator extends Validator<EventLocationDto> {
    private final CountryMapper countryMapper;
    private final LocationValidator locationValidator;

    public EventLocationValidator(CountryMapper countryMapper,
                                  LocationValidator locationValidator) {
        this.countryMapper = countryMapper;
        this.locationValidator = locationValidator;
    }

    @Override
    public void getMapsForCheck(EventLocationDto eventLocationDto) {
        super.STRINGS_FOR_BLANK_CHECK = ImmutableMap.<String, String>builder()
                .put("address", eventLocationDto.getAddress())
                .put("city name", eventLocationDto.getLocation().getName())
                .build();
        super.OBJECT_NULL_CHECK = ImmutableMap.<String, Optional<Object>>builder()
                .put("end location", Optional.ofNullable(eventLocationDto.getIsEnd()))
                .put("start location", Optional.ofNullable(eventLocationDto.getIsStart()))
                .build();
    }

    @Override
    protected String commonValidateCustom(EventLocationDto eventLocationDto) {
        StringBuilder errors = new StringBuilder();
        Country country;
        if (Strings.isNullOrEmpty(eventLocationDto.getLocation().getCountry().getName())) {
            errors.append("Not entered country name ");
            return errors.toString();
        } else {
            country = countryMapper.dtoToEntity(eventLocationDto.getLocation().getCountry());
        }
        if (country == null) {
            errors.append("Not entered country name ");
        }

        String validateLocation = locationValidator.validate(eventLocationDto.getLocation());
        if (StringUtils.isNotBlank(validateLocation)) {
            errors.append(validateLocation);
        }
        return errors.toString();
    }
}
