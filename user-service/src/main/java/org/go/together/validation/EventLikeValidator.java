package org.go.together.validation;

import lombok.RequiredArgsConstructor;
import org.go.together.dto.EventLikeDto;
import org.go.together.enums.CrudOperation;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.model.EventLike;
import org.go.together.repository.interfaces.EventLikeRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventLikeValidator extends CommonValidator<EventLikeDto> {
    private final EventLikeRepository eventLikeRepository;

    @Override
    protected String commonValidation(UUID requestId, EventLikeDto dto, CrudOperation crudOperation) {
        StringBuilder errors = new StringBuilder();

        if (crudOperation == CrudOperation.UPDATE) {
            EventLike eventLike = eventLikeRepository.findById(dto.getId())
                    .orElseThrow(() -> new CannotFindEntityException("Cannot find EventLike for event " + dto.getEventId()));
            if (!eventLike.getEventId().equals(dto.getEventId())) {
                errors.append("Not correct event id expected: ")
                        .append(eventLike.getEventId())
                        .append(" but was ")
                        .append(dto.getEventId());
            }
        }
        return errors.toString();
    }
}
