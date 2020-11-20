package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.base.FindService;
import org.go.together.dto.EventDto;

public interface EventService extends CrudService<EventDto>, FindService<EventDto> {
}
