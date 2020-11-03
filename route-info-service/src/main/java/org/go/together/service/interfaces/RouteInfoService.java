package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.dto.RouteInfoDto;
import org.go.together.find.FindService;
import org.go.together.model.RouteInfo;

public interface RouteInfoService extends CrudService<RouteInfoDto>, FindService<RouteInfo> {
}
