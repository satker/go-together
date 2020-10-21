package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.dto.EventDto;
import org.go.together.dto.SimpleDto;
import org.go.together.find.FindService;
import org.go.together.model.Event;

import java.util.Set;

public interface EventService extends CrudService<EventDto>, FindService<Event> {
    Set<SimpleDto> autocompleteEvents(String name);
}
