package org.go.together.validation;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Strings;
import org.go.together.dto.LocationDto;
import org.go.together.enums.CrudOperation;
import org.go.together.mapper.CountryMapper;
import org.go.together.model.Country;
import org.springframework.stereotype.Component;

import java.util.Map;

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
        super.STRINGS_FOR_BLANK_CHECK = Map.of(
                "address", LocationDto::getAddress,
                "city name", dto -> dto.getPlace().getName());
        super.OBJECT_NULL_CHECK = Map.of(
                "place", LocationDto::getPlace,
                "end location", LocationDto::getIsEnd,
                "start location", LocationDto::getIsStart);
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
