package org.go.together.service;

import org.go.together.dto.EventDto;
import org.go.together.logic.CrudService;
import org.go.together.mapper.EventMapper;
import org.go.together.model.Event;
import org.go.together.repository.EventRepository;
import org.go.together.validation.EventValidator;
import org.springframework.stereotype.Service;

@Service
public class EventService extends CrudService<EventDto, Event> {
    protected EventService(EventRepository eventRepository,
                           EventMapper eventMapper,
                           EventValidator eventValidator) {
        super(eventRepository, eventMapper, eventValidator);
    }
}
