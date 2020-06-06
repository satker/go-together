package org.go.together.validation;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.client.UserClient;
import org.go.together.dto.CashCategory;
import org.go.together.dto.EventDto;
import org.go.together.dto.EventPaidThingDto;
import org.go.together.dto.UserDto;
import org.go.together.dto.validation.DateIntervalDto;
import org.go.together.logic.Validator;
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
        super.COLLECTION_CORRECT_CHECK = ImmutableMap.<String, Collection<?>>builder()
                .put("photos", dto.getEventPhotoDto().getPhotos())
                .put("routes", dto.getRoute())
                .build();
    }

    @Override
    protected String commonValidateCustom(EventDto dto) {
        StringBuilder errors = new StringBuilder();

        if (Optional.ofNullable(dto.getAuthor()).map(UserDto::getId).isEmpty()) {
            errors.append("Should be an event author. ");
        }

        if (!userClient.checkIfUserPresentsById(dto.getAuthor().getId())) {
            errors.append("Author has incorrect uuid: ")
                    .append(dto.getAuthor().getId())
                    .append(". ");
        }


        List<CashCategory> cashCategories = dto.getPaidThings().stream()
                .map(EventPaidThingDto::getCashCategory)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (cashCategories.size() != dto.getPaidThings().size()) {
            errors.append("Collection paid things is incorrect.");
        } else {
            dto.getPaidThings().stream()
                    .map(eventPaidThingValidator::validate)
                    .filter(StringUtils::isNotBlank)
                    .forEach(errors::append);
        }

        dto.getEventPhotoDto().getPhotos().stream()
                .map(contentClient::validate)
                .filter(StringUtils::isNotBlank)
                .forEach(errors::append);

        dto.getRoute().stream()
                .map(locationClient::validateRoutes)
                .filter(StringUtils::isNotBlank)
                .forEach(errors::append);

        return errors.toString();
    }
}
