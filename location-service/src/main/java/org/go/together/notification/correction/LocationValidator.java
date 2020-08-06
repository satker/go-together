package org.go.together.notification.correction;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Strings;
import org.go.together.enums.CrudOperation;
import org.go.together.mapper.CountryMapper;
import org.go.together.model.Country;
import org.go.together.notification.dto.LocationDto;
import org.go.together.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LocationValidator extends Validator<LocationDto> {
    private final CountryMapper countryMapper;
    private final PlaceValidator placeValidator;

    public LocationValidator(CountryMapper countryMapper,
                             PlaceValidator placeValidator) {
        this.countryMapper = countryMapper;
        this.placeValidator = placeValidator;
    }

    @Override
    public void getMapsForCheck(LocationDto locationDto) {
        super.STRINGS_FOR_BLANK_CHECK = ImmutableMap.<String, String>builder()
                .put("address", locationDto.getAddress())
                .put("city name", locationDto.getPlace().getName())
                .build();
        super.OBJECT_NULL_CHECK = ImmutableMap.<String, Optional<Object>>builder()
                .put("place", Optional.ofNullable(locationDto.getPlace()))
                .put("end location", Optional.ofNullable(locationDto.getIsEnd()))
                .put("start location", Optional.ofNullable(locationDto.getIsStart()))
                .build();
    }

    @Override
    protected String commonValidation(LocationDto locationDto, CrudOperation crudOperation) {
        StringBuilder errors = new StringBuilder();
        Country country;
        if (Strings.isNullOrEmpty(locationDto.getPlace().getCountry().getName())) {
            errors.append("Not entered country name ");
            return errors.toString();
        } else {
            country = countryMapper.dtoToEntity(locationDto.getPlace().getCountry());
        }
        if (country == null) {
            errors.append("Not entered country name ");
        }

        String validateLocation = placeValidator.validate(locationDto.getPlace(), crudOperation);
        if (StringUtils.isNotBlank(validateLocation)) {
            errors.append(validateLocation);
        }
        return errors.toString();
    }
}
