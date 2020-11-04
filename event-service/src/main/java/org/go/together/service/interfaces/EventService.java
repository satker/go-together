package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.base.FindService;
import org.go.together.dto.EventDto;
import org.go.together.dto.SimpleDto;

import java.util.Set;

public interface EventService extends CrudService<EventDto>, FindService<EventDto> {
    Set<SimpleDto> autocompleteEvents(String name);
}
