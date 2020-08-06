package org.go.together.correction;

import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.GroupLocationDto;
import org.go.together.dto.LocationCategory;
import org.go.together.dto.LocationDto;
import org.go.together.enums.CrudOperation;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class GroupLocationValidator extends Validator<GroupLocationDto> {
    private final LocationValidator locationValidator;

    public GroupLocationValidator(LocationValidator locationValidator) {
        this.locationValidator = locationValidator;
    }

    @Override
    public void getMapsForCheck(GroupLocationDto dto) {

    }

    @Override
    protected String commonValidation(GroupLocationDto dto, CrudOperation crudOperation) {
        StringBuilder errors = new StringBuilder();
        checkRoutes(dto.getLocations(), errors, crudOperation);
        if (dto.getCategory().equals(LocationCategory.EVENT)) {
            boolean presentEndRoute = dto.getLocations().stream().anyMatch(LocationDto::getIsEnd);
            boolean presentStartRoute = dto.getLocations().stream().anyMatch(LocationDto::getIsStart);
            if (!presentEndRoute) {
                errors.append("End route not present");
            }
            if (!presentStartRoute) {
                errors.append("Start route not present");
            }
        }
        return errors.toString();
    }

    private void checkRoutes(Collection<LocationDto> routes, StringBuilder errors, CrudOperation crudOperation) {
        Set<Integer> numbers = IntStream.rangeClosed(1, routes.size())
                .boxed()
                .collect(Collectors.toSet());
        boolean isRouteNumbersCorrect = routes.stream()
                .map(LocationDto::getRouteNumber)
                .allMatch(numbers::contains);
        if (isRouteNumbersCorrect) {
            routes.stream()
                    .map(locationDto -> locationValidator.validate(locationDto, crudOperation))
                    .filter(StringUtils::isNotBlank)
                    .forEach(errors::append);
        } else {
            errors.append("Incorrect route numbers");
        }
    }
}
