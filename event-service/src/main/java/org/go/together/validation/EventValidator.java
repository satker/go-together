package org.go.together.validation;

import lombok.RequiredArgsConstructor;
import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.client.RouteInfoClient;
import org.go.together.client.UserClient;
import org.go.together.dto.EventDto;
import org.go.together.enums.CrudOperation;
import org.go.together.validation.dto.DateIntervalDto;
import org.go.together.validation.impl.CommonValidator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class EventValidator extends CommonValidator<EventDto> {
    private final UserClient userClient;
    private final ContentClient contentClient;
    private final LocationClient locationClient;
    private final RouteInfoClient routeInfoClient;

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
        super.ANOTHER_SERVICE_DTO_CORRECT_CHECK = Map.of(
                contentClient, dto.getGroupPhoto(),
                locationClient, dto.getRoute(),
                routeInfoClient, dto.getRouteInfo()
        );
    }

    @Override
    protected String commonValidation(EventDto dto, CrudOperation crudOperation) {
        StringBuilder errors = new StringBuilder();

        if (!userClient.checkIfUserPresentsById(dto.getAuthor().getId())) {
            errors.append("Author has incorrect uuid: ")
                    .append(dto.getAuthor().getId())
                    .append(". ");
        }

        return errors.toString();
    }
}
