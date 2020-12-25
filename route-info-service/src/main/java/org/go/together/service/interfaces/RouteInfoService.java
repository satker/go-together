package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.base.FindService;
import org.go.together.dto.RouteInfoDto;
import org.go.together.model.RouteInfo;

import java.util.Optional;
import java.util.UUID;

public interface RouteInfoService extends CrudService<RouteInfoDto>, FindService<RouteInfoDto> {
    Optional<RouteInfo> readRouteInfo(UUID id);
}
