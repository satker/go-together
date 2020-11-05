package org.go.together.service.impl;

import org.go.together.base.CommonCrudService;
import org.go.together.dto.RouteInfoDto;
import org.go.together.model.RouteInfo;
import org.go.together.service.interfaces.RouteInfoService;
import org.springframework.stereotype.Service;

@Service
public class RouteInfoServiceImpl extends CommonCrudService<RouteInfoDto, RouteInfo>
        implements RouteInfoService {
    @Override
    public String getServiceName() {
        return "routeInfo";
    }
}
