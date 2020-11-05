package org.go.together.validation;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Strings;
import org.go.together.base.Mapper;
import org.go.together.base.Validator;
import org.go.together.dto.CountryDto;
import org.go.together.dto.LocationDto;
import org.go.together.dto.PlaceDto;
import org.go.together.enums.CrudOperation;
import org.go.together.model.Country;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class LocationValidator extends CommonValidator<LocationDto> {
    private final Mapper<CountryDto, Country> countryMapper;
    private final Validator<PlaceDto> placeValidator;

    @Override
    public Map<String, Function<LocationDto, ?>> getMapsForCheck() {
        return Map.of(
                "address", LocationDto::getAddress,
                "city name", dto -> dto.getPlace().getName(),
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
