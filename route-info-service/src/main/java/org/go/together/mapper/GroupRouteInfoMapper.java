package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.CommonMapper;
import org.go.together.base.Mapper;
import org.go.together.dto.GroupRouteInfoDto;
import org.go.together.dto.RouteInfoDto;
import org.go.together.model.GroupRouteInfo;
import org.go.together.model.RouteInfo;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GroupRouteInfoMapper extends CommonMapper<GroupRouteInfoDto, GroupRouteInfo> {
    private final Mapper<RouteInfoDto, RouteInfo> routeInfoMapper;

    @Override
    public GroupRouteInfoDto toDto(GroupRouteInfo entity) {
        GroupRouteInfoDto groupRouteInfoDto = new GroupRouteInfoDto();
        groupRouteInfoDto.setGroupId(entity.getGroupId());
        groupRouteInfoDto.setId(entity.getId());
        groupRouteInfoDto.setInfoRoutes(routeInfoMapper.entitiesToDtos(entity.getInfoRoutes()));
        return groupRouteInfoDto;
    }

    @Override
    public GroupRouteInfo toEntity(GroupRouteInfoDto dto) {
        GroupRouteInfo groupRouteInfo = new GroupRouteInfo();
        groupRouteInfo.setGroupId(dto.getGroupId());
        groupRouteInfo.setInfoRoutes(dto.getInfoRoutes().stream()
                .map(routeInfoMapper::dtoToEntity)
                .collect(Collectors.toSet()));
        groupRouteInfo.setId(dto.getId());
        return groupRouteInfo;
    }
}
