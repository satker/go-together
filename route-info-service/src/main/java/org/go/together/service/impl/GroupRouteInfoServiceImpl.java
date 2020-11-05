package org.go.together.service.impl;

import org.go.together.base.CommonCrudService;
import org.go.together.dto.GroupRouteInfoDto;
import org.go.together.model.GroupRouteInfo;
import org.go.together.service.interfaces.GroupRouteInfoService;
import org.springframework.stereotype.Service;

@Service
public class GroupRouteInfoServiceImpl extends CommonCrudService<GroupRouteInfoDto, GroupRouteInfo>
        implements GroupRouteInfoService {
    @Override
    public String getServiceName() {
        return "groupRouteInfo";
    }
}
