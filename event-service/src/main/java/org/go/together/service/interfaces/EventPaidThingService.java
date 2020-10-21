package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.dto.EventPaidThingDto;
import org.go.together.find.FindService;
import org.go.together.model.EventPaidThing;

public interface EventPaidThingService extends CrudService<EventPaidThingDto>, FindService<EventPaidThing> {
}
