package org.go.together.validation;

import lombok.RequiredArgsConstructor;
import org.go.together.dto.*;
import org.go.together.enums.CrudOperation;
import org.go.together.enums.FindOperator;
import org.go.together.kafka.producers.FindProducer;
import org.go.together.repository.interfaces.EventRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class EventUserValidator extends CommonValidator<EventUserDto> {
    private final FindProducer<UserDto> findUserKafkaProducer;
    private final EventRepository eventRepository;

    @Override
    public Map<String, Function<EventUserDto, ?>> getMapsForCheck(UUID requestId) {
        return Map.of(
                "user id", testDto -> testDto.getUser().getId(),
                "user status", EventUserDto::getUserStatus);
    }

    @Override
    protected String commonValidation(UUID requestId, EventUserDto dto, CrudOperation crudOperation) {
        StringBuilder errors = new StringBuilder();

        if (isNotPresentUser(requestId, dto.getUser().getId())) {
            errors.append("Author has incorrect uuid: ")
                    .append(dto.getUser().getId())
                    .append(". ");
        }

        if (eventRepository.findById(dto.getEventId()).isEmpty()) {
            errors.append("Could not found event by uuid: ")
                    .append(dto.getEventId())
                    .append(". ");
        }

        return errors.toString();
    }

    private boolean isNotPresentUser(UUID requestId, UUID authorId) {
        FormDto formDto = new FormDto();
        FilterDto filterDto = new FilterDto();
        filterDto.setValues(Collections.singleton(Collections.singletonMap("id", new FilterValueDto(FindOperator.EQUAL, authorId))));
        formDto.setFilters(Collections.singletonMap("id", filterDto));
        formDto.setMainIdField("users.id");
        return findUserKafkaProducer.find(requestId, formDto).getResult().isEmpty();
    }
}
