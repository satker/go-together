package org.go.together.validation;

import lombok.RequiredArgsConstructor;
import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.client.RouteInfoClient;
import org.go.together.client.UserClient;
import org.go.together.dto.EventDto;
import org.go.together.enums.CrudOperation;
import org.go.together.validation.dto.DateIntervalDto;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class EventValidator extends CommonValidator<EventDto> {
    private final UserClient userClient;
    private final ContentClient contentClient;
    private final LocationClient locationClient;
    private final RouteInfoClient routeInfoClient;

    @Override
    public Map<String, Function<EventDto, ?>> getMapsForCheck() {
        return Map.of(
                "event name", EventDto::getName,
                "event description", EventDto::getDescription,
                "event people capacity", EventDto::getPeopleCount,
                "event dates", eventDto -> new DateIntervalDto(eventDto.getStartDate(), eventDto.getEndDate()),
                "routes", EventDto::getRoute,
                "photos", eventDto -> eventDto.getGroupPhoto().getPhotos(),
                "routes locations", eventDto -> eventDto.getRoute().getLocations(),
                "event photos", eventDto -> contentClient.validate("groupPhotos", eventDto.getGroupPhoto()),
                "event locations", eventDto -> locationClient.validate("groupLocations", eventDto.getRoute())
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
