package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.base.FindService;
import org.go.together.dto.EventUserDto;

public interface EventUserService extends CrudService<EventUserDto>, FindService<EventUserDto> {
    boolean deleteEventUserByEventId(EventUserDto eventUserDto);
}
