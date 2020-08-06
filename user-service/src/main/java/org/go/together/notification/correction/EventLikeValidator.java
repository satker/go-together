package org.go.together.notification.correction;

import org.go.together.enums.CrudOperation;
import org.go.together.model.EventLike;
import org.go.together.notification.dto.EventLikeDto;
import org.go.together.notification.repository.EventLikeRepository;
import org.go.together.repository.exceptions.CannotFindEntityException;
import org.go.together.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventLikeValidator extends Validator<EventLikeDto> {
    @Autowired
    private EventLikeRepository eventLikeRepository;

    @Override
    public void getMapsForCheck(EventLikeDto dto) {

    }

    @Override
    protected String commonValidation(EventLikeDto dto, CrudOperation crudOperation) {
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
