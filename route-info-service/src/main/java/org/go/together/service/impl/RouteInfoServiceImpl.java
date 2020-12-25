package org.go.together.service.impl;

import org.go.together.base.CommonCrudService;
import org.go.together.dto.RouteInfoDto;
import org.go.together.model.RouteInfo;
import org.go.together.service.interfaces.RouteInfoService;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class RouteInfoServiceImpl extends CommonCrudService<RouteInfoDto, RouteInfo>
        implements RouteInfoService {

    @Override
    public Optional<RouteInfo> readRouteInfo(UUID id) {
        if (Objects.isNull(id)) {
            return Optional.empty();
        }
        return repository.findById(id);
    }

    @Override
    public String getServiceName() {
        return "routeInfo";
    }
}
