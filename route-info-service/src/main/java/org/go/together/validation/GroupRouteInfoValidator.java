package org.go.together.validation;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.go.together.base.Validator;
import org.go.together.dto.GroupRouteInfoDto;
import org.go.together.dto.RouteInfoDto;
import org.go.together.enums.CrudOperation;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class GroupRouteInfoValidator extends CommonValidator<GroupRouteInfoDto> {
    private final Validator<RouteInfoDto> routeInfoValidator;

    @Override
    protected String commonValidation(UUID requestId,
                                      GroupRouteInfoDto dto,
                                      CrudOperation crudOperation) {
        StringBuilder errors = new StringBuilder();
        checkRoutes(dto.getInfoRoutes(), errors, crudOperation);
        boolean presentEndRoute = dto.getInfoRoutes().stream().anyMatch(RouteInfoDto::getIsEnd);
        boolean presentStartRoute = dto.getInfoRoutes().stream().anyMatch(RouteInfoDto::getIsStart);
        if (!presentEndRoute) {
            errors.append("End route not present");
        }
        if (!presentStartRoute) {
            errors.append("Start route not present");
        }
        return errors.toString();
    }

    private void checkRoutes(Collection<RouteInfoDto> routes, StringBuilder errors, CrudOperation crudOperation) {
        Set<Integer> numbers = IntStream.rangeClosed(1, routes.size())
                .boxed()
                .collect(Collectors.toSet());
        boolean isRouteNumbersCorrect = routes.stream()
                .map(RouteInfoDto::getRouteNumber)
                .allMatch(numbers::contains);
        if (isRouteNumbersCorrect) {
            routes.stream()
                    .map(locationDto -> routeInfoValidator.validate(locationDto, crudOperation))
                    .filter(StringUtils::isNotBlank)
                    .forEach(errors::append);
        } else {
            errors.append("Incorrect route numbers");
        }
    }
}
