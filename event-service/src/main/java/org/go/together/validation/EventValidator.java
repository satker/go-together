package org.go.together.validation;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.client.UserClient;
import org.go.together.dto.EventDto;
import org.go.together.dto.UserDto;
import org.go.together.logic.Validator;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

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
        super.NUMBER_CORRECT_NEGATIVE_CHECK = ImmutableMap.<String, Number>builder()
                .put("event likes", dto.getPeopleLike())
                .build();
        super.NUMBER_CORRECT_ZERO_OR_NEGATIVE_CHECK = ImmutableMap.<String, Number>builder()
                .put("event people capacity", dto.getPeopleCount())
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

        Optional.ofNullable(dto.getUsers())
                .orElse(Collections.emptySet()).stream()
                .map(UserDto::getId)
                .filter(userDtoId -> !userClient.checkIfUserPresentsById(userDtoId))
                .map(userDtoId -> "User has incorrect uuid: " + userDtoId + ". ")
                .forEach(errors::append);


        dto.getPaidThings().stream()
                .map(eventPaidThingValidator::validate)
                .filter(StringUtils::isNotBlank)
                .forEach(errors::append);

        dto.getEventPhotoDto().getPhotos().stream()
                .map(contentClient::validate)
                .filter(StringUtils::isNotBlank)
                .forEach(errors::append);

        dto.getRoute().stream()
                .map(locationClient::validate)
                .filter(StringUtils::isNotBlank)
                .forEach(errors::append);

        return errors.toString();
    }
}
