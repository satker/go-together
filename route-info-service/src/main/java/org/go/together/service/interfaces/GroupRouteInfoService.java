package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.dto.GroupRouteInfoDto;
import org.go.together.find.FindService;
import org.go.together.model.GroupRouteInfo;

public interface GroupRouteInfoService extends CrudService<GroupRouteInfoDto>, FindService<GroupRouteInfo> {
}
