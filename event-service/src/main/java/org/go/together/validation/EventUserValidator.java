package org.go.together.validation;

import com.google.common.collect.ImmutableMap;
import org.go.together.client.UserClient;
import org.go.together.dto.EventUserDto;
import org.go.together.enums.CrudOperation;
import org.go.together.logic.Validator;
import org.go.together.repository.EventRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EventUserValidator extends Validator<EventUserDto> {
    private final UserClient userClient;
    private final EventRepository eventRepository;

    public EventUserValidator(UserClient userClient, EventRepository eventRepository) {
        this.userClient = userClient;
        this.eventRepository = eventRepository;
    }

    @Override
    public void getMapsForCheck(EventUserDto dto) {
        super.OBJECT_NULL_CHECK = ImmutableMap.<String, Optional<Object>>builder()
                .put("user id", Optional.ofNullable(dto.getUser().getId()))
                .put("user status", Optional.ofNullable(dto.getUserStatus()))
                .build();
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
