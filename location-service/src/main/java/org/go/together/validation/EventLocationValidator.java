package org.go.together.validation;

import com.google.common.collect.ImmutableMap;
import org.assertj.core.util.Strings;
import org.go.together.dto.EventLocationDto;
import org.go.together.logic.Validator;
import org.go.together.mapper.CountryMapper;
import org.go.together.model.Country;
import org.springframework.stereotype.Component;

@Component
public class EventLocationValidator extends Validator<EventLocationDto> {
    private final CountryMapper countryMapper;

    public EventLocationValidator(CountryMapper countryMapper) {
        this.countryMapper = countryMapper;
    }

    @Override
    public void getMapsForCheck(EventLocationDto eventLocationDto) {
        super.STRINGS_FOR_BLANK_CHECK = ImmutableMap.<String, String>builder()
                .put("address", eventLocationDto.getAddress())
                .put("city name", eventLocationDto.getLocation().getName())
                .build();
    }

    @Override
    protected String commonValidateCustom(EventLocationDto eventLocationDto) {
        StringBuilder errors = new StringBuilder();
        Country country;
        if (Strings.isNullOrEmpty(eventLocationDto.getLocation().getCountry().getName())) {
            errors.append("Не задано название страны. ");
            return errors.toString();
        } else {
            country = countryMapper.dtoToEntity(eventLocationDto.getLocation().getCountry());
        }
        if (country == null) {
            errors.append("Некорректное название страны. ");
        }
        return errors.toString();
    }
}
