package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.dto.GroupLocationDto;
import org.go.together.find.FindService;
import org.go.together.model.GroupLocation;

public interface GroupLocationService extends CrudService<GroupLocationDto>, FindService<GroupLocation> {
}
