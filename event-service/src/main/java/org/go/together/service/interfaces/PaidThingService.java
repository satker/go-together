package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.dto.PaidThingDto;
import org.go.together.find.FindService;
import org.go.together.model.PaidThing;

public interface PaidThingService extends CrudService<PaidThingDto>, FindService<PaidThing> {
}
