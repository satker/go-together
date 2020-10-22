package org.go.together.validation;

import lombok.RequiredArgsConstructor;
import org.go.together.client.UserClient;
import org.go.together.dto.EventUserDto;
import org.go.together.enums.CrudOperation;
import org.go.together.repository.interfaces.EventRepository;
import org.go.together.validation.impl.CommonValidator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class EventUserValidator extends CommonValidator<EventUserDto> {
    private final UserClient userClient;
    private final EventRepository eventRepository;

    @Override
    public void getMapsForCheck(EventUserDto dto) {
        super.OBJECT_NULL_CHECK = Map.of(
                "user id", testDto -> testDto.getUser().getId(),
                "user status", EventUserDto::getUserStatus);
    }

    @Override
    protected String commonValidation(EventUserDto dto, CrudOperation crudOperation) {
        StringBuilder errors = new StringBuilder();

        if (!userClient.checkIfUserPresentsById(dto.getUser().getId())) {
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
}
