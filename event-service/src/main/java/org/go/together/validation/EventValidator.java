package org.go.together.validation;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.client.UserClient;
import org.go.together.dto.EventDto;
import org.go.together.dto.UserDto;
import org.go.together.enums.CrudOperation;
import org.go.together.validation.dto.DateIntervalDto;
import org.go.together.validation.impl.CommonValidator;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventValidator extends CommonValidator<EventDto> {
    private final UserClient userClient;
    private final ContentClient contentClient;
    private final LocationClient locationClient;

    @Override
    public void getMapsForCheck(EventDto dto) {
        super.STRINGS_FOR_BLANK_CHECK = Map.of(
                "event name", EventDto::getName,
                "event description", EventDto::getDescription);
        super.NUMBER_CORRECT_ZERO_OR_NEGATIVE_CHECK = Map.of(
                "event people capacity", EventDto::getPeopleCount);
        super.DATES_CORRECT_CHECK = Map.of(
                "event dates", new DateIntervalDto(dto.getStartDate(), dto.getEndDate()));
        super.OBJECT_NULL_CHECK = Map.of(
                "routes", EventDto::getRoute);
        super.COLLECTION_CORRECT_CHECK = Map.of(
                "photos", testDto -> testDto.getGroupPhoto().getPhotos(),
                "routes", testDto -> testDto.getRoute().getLocations());
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


        String contentValidation = contentClient.validate(dto.getGroupPhoto());
        if (StringUtils.isNotBlank(contentValidation)) {
            errors.append(contentValidation);
        }

        locationClient.validateRoute(dto.getRoute());

        return errors.toString();
    }
}
