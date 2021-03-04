package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.Mapper;
import org.go.together.dto.GroupRouteInfoDto;
import org.go.together.dto.RouteInfoDto;
import org.go.together.model.GroupRouteInfo;
import org.go.together.model.RouteInfo;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GroupRouteInfoMapper implements Mapper<GroupRouteInfoDto, GroupRouteInfo> {
    private final Mapper<RouteInfoDto, RouteInfo> routeInfoMapper;

    @Override
    public GroupRouteInfoDto entityToDto(GroupRouteInfo entity) {
        GroupRouteInfoDto groupRouteInfoDto = new GroupRouteInfoDto();
        groupRouteInfoDto.setGroupId(entity.getGroupId());
        groupRouteInfoDto.setId(entity.getId());
        groupRouteInfoDto.setInfoRoutes(entity.getInfoRoutes().stream()
                .map(routeInfo -> routeInfoMapper.entityToDto(routeInfo))
                .collect(Collectors.toSet()));
        return groupRouteInfoDto;
    }

    @Override
    public GroupRouteInfo dtoToEntity(GroupRouteInfoDto dto) {
        GroupRouteInfo groupRouteInfo = new GroupRouteInfo();
        groupRouteInfo.setGroupId(dto.getGroupId());
        groupRouteInfo.setInfoRoutes(dto.getInfoRoutes().stream()
                .map(routeInfoMapper::dtoToEntity)
                .collect(Collectors.toSet()));
        groupRouteInfo.setId(dto.getId());
        return groupRouteInfo;
    }
}
