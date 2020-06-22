package org.go.together.validation;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.client.UserClient;
import org.go.together.dto.*;
import org.go.together.dto.validation.DateIntervalDto;
import org.go.together.enums.CrudOperation;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class EventValidator extends Validator<EventDto> {
    private final UserClient userClient;
    private final ContentClient contentClient;
    private final LocationClient locationClient;
    private final EventPaidThingValidator eventPaidThingValidator;

    public EventValidator(UserClient userClient,
                          ContentClient contentClient,
                          LocationClient locationClient,
                          EventPaidThingValidator eventPaidThingValidator) {
        this.userClient = userClient;
        this.contentClient = contentClient;
        this.locationClient = locationClient;
        this.eventPaidThingValidator = eventPaidThingValidator;
    }

    @Override
    public void getMapsForCheck(EventDto dto) {
        super.STRINGS_FOR_BLANK_CHECK = ImmutableMap.<String, String>builder()
                .put("event name", dto.getName())
                .put("event description", dto.getDescription())
                .build();
        super.NUMBER_CORRECT_ZERO_OR_NEGATIVE_CHECK = ImmutableMap.<String, Number>builder()
                .put("event people capacity", dto.getPeopleCount())
                .build();
        super.DATES_CORRECT_CHECK = ImmutableMap.<String, DateIntervalDto>builder()
                .put("event dates", new DateIntervalDto(dto.getStartDate(), dto.getEndDate()))
                .build();
        super.COLLECTION_CORRECT_CHECK = ImmutableMap.<String, Collection<?>>builder()
                .put("photos", dto.getEventPhotoDto().getPhotos())
                .put("routes", dto.getRoute())
                .build();
    }

    @Override
    protected String commonValidation(EventDto dto, CrudOperation crudOperation) {
        StringBuilder errors = new StringBuilder();

        if (Optional.ofNullable(dto.getAuthor()).map(UserDto::getId).isEmpty()) {
            errors.append("Should be an event author. ");
        }

        if (!userClient.checkIfUserPresentsById(dto.getAuthor().getId())) {
            errors.append("Author has incorrect uuid: ")
                    .append(dto.getAuthor().getId())
                    .append(". ");
        }

        checkCashCategories(dto.getPaidThings(), errors, crudOperation);

        dto.getEventPhotoDto().getPhotos().stream()
                .map(contentClient::validate)
                .filter(StringUtils::isNotBlank)
                .forEach(errors::append);

        checkRoutes(dto.getRoute(), errors);

        return errors.toString();
    }

    private void checkCashCategories(Collection<EventPaidThingDto> paidThingDtos, StringBuilder errors,
                                     CrudOperation crudOperation) {
        List<CashCategory> cashCategories = paidThingDtos.stream()
                .map(EventPaidThingDto::getCashCategory)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (cashCategories.size() != paidThingDtos.size()) {
            errors.append("Collection paid things is incorrect.");
        } else {
            paidThingDtos.stream()
                    .map(paidThingDto -> eventPaidThingValidator.validate(paidThingDto, crudOperation))
                    .filter(StringUtils::isNotBlank)
                    .forEach(errors::append);
        }
    }

    private void checkRoutes(Collection<EventLocationDto> routes, StringBuilder errors) {
        Set<Integer> numbers = IntStream.rangeClosed(1, routes.size())
                .boxed()
                .collect(Collectors.toSet());
        boolean isRouteNumbersCorrect = routes.stream()
                .map(EventLocationDto::getRouteNumber)
                .allMatch(numbers::contains);
        if (isRouteNumbersCorrect) {
            routes.stream()
                    .map(locationClient::validateRoutes)
                    .filter(StringUtils::isNotBlank)
                    .forEach(errors::append);
        } else {
            errors.append("Incorrect route numbers");
        }

        boolean presentEndRoute = routes.stream().anyMatch(EventLocationDto::getIsEnd);
        boolean presentStartRoute = routes.stream().anyMatch(EventLocationDto::getIsStart);
        if (!presentEndRoute) {
            errors.append("End route not present");
        }
        if (!presentStartRoute) {
            errors.append("Start route not present");
        }
    }
}
