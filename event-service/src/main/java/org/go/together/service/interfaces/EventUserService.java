package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.dto.EventUserDto;
import org.go.together.find.FindService;
import org.go.together.model.EventUser;

public interface EventUserService extends CrudService<EventUserDto>, FindService<EventUser> {
    boolean deleteEventUserByEventId(EventUserDto eventUserDto);
}
