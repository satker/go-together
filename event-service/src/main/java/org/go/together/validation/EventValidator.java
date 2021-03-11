package org.go.together.validation;

import lombok.RequiredArgsConstructor;
import org.go.together.dto.*;
import org.go.together.enums.CrudOperation;
import org.go.together.enums.FindOperator;
import org.go.together.kafka.producers.FindProducer;
import org.go.together.kafka.producers.ValidationProducer;
import org.go.together.validation.dto.DateIntervalDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class EventValidator extends CommonValidator<EventDto> {
    private final FindProducer<UserDto> findUserKafkaProducer;
    private final ValidationProducer<GroupPhotoDto> photoValidator;
    private final ValidationProducer<GroupRouteInfoDto> routeInfoValidator;

    @Override
    public Map<String, Function<EventDto, ?>> getMapsForCheck() {
        return Map.of(
                "event name", EventDto::getName,
                "event description", EventDto::getDescription,
                "event people capacity", EventDto::getPeopleCount,
                "event dates", eventDto -> new DateIntervalDto(eventDto.getStartDate(), eventDto.getEndDate()),
                "photos", eventDto -> eventDto.getGroupPhoto().getPhotos(),
                "event photos", eventDto -> photoValidator.validate(eventDto.getGroupPhoto()),
                "routes info", eventDto -> routeInfoValidator.validate(eventDto.getRouteInfo())
        );
    }

    @Override
    protected String commonValidation(EventDto dto, CrudOperation crudOperation) {
        StringBuilder errors = new StringBuilder();

        if (isNotPresentUser(dto.getAuthor().getId())) {
            errors.append("Author has incorrect uuid: ")
                    .append(dto.getAuthor().getId())
                    .append(". ");
        }

        return errors.toString();
    }

    private boolean isNotPresentUser(UUID authorId) {
        FilterDto filterDto = new FilterDto();
        filterDto.setValues(Collections.singleton(Collections.singletonMap("id", new FilterValueDto(FindOperator.EQUAL, authorId))));
        FormDto formDto = new FormDto();
        formDto.setFilters(Collections.singletonMap("id", filterDto));
        formDto.setMainIdField("users.id");
        return findUserKafkaProducer.find(formDto).getResult().isEmpty();
    }
}
