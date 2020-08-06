package org.go.together.notification.correction;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.validation.DateIntervalDto;
import org.go.together.enums.CrudOperation;
import org.go.together.notification.client.ContentClient;
import org.go.together.notification.client.LocationClient;
import org.go.together.notification.client.UserClient;
import org.go.together.notification.dto.CashCategory;
import org.go.together.notification.dto.EventDto;
import org.go.together.notification.dto.EventPaidThingDto;
import org.go.together.notification.dto.UserDto;
import org.go.together.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
        super.OBJECT_NULL_CHECK = ImmutableMap.<String, Optional<Object>>builder()
                .put("routes", Optional.ofNullable(dto.getRoute()))
                .build();
        super.COLLECTION_CORRECT_CHECK = ImmutableMap.<String, Collection<?>>builder()
                .put("photos", dto.getGroupPhoto().getPhotos())
                .put("routes", dto.getRoute().getLocations())
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

        String contentValidation = contentClient.validate(dto.getGroupPhoto());
        if (StringUtils.isNotBlank(contentValidation)) {
            errors.append(contentValidation);
        }

        locationClient.validateRoute(dto.getRoute());

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
}
