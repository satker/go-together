package org.go.together.service.impl;

import lombok.RequiredArgsConstructor;
import org.go.together.base.CommonCrudService;
import org.go.together.dto.GroupRouteInfoDto;
import org.go.together.dto.IdDto;
import org.go.together.dto.RouteInfoDto;
import org.go.together.enums.CrudOperation;
import org.go.together.exceptions.ApplicationException;
import org.go.together.model.GroupRouteInfo;
import org.go.together.model.RouteInfo;
import org.go.together.service.interfaces.GroupRouteInfoService;
import org.go.together.service.interfaces.RouteInfoService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupRouteInfoServiceImpl extends CommonCrudService<GroupRouteInfoDto, GroupRouteInfo>
        implements GroupRouteInfoService {
    private final RouteInfoService routeInfoService;

    @Override
    protected GroupRouteInfo enrichEntity(GroupRouteInfo entity, GroupRouteInfoDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.CREATE || crudOperation == CrudOperation.UPDATE) {
            Set<RouteInfo> routeInfos = dto.getInfoRoutes().stream()
                    .map(this::getRouteInfo)
                    .collect(Collectors.toSet());
            GroupRouteInfo groupRouteInfo = repository.findByIdOrThrow(entity.getId());
            deleteUnneededRouteInfos(groupRouteInfo.getInfoRoutes(), routeInfos);
            entity.setInfoRoutes(routeInfos);
        } else if (crudOperation == CrudOperation.DELETE) {
            entity.getInfoRoutes().stream()
                    .map(RouteInfo::getId)
                    .forEach(routeInfoService::delete);
        }
        return entity;
    }

    private RouteInfo getRouteInfo(RouteInfoDto routeInfoDto) {
        Optional<RouteInfo> routeInfo = routeInfoService.readRouteInfo(routeInfoDto.getId());
        if (routeInfo.isEmpty()) {
            IdDto createdRouteInfoId = routeInfoService.create(routeInfoDto);
            Optional<RouteInfo> createdRouteInfo = routeInfoService.readRouteInfo(createdRouteInfoId.getId());
            return createdRouteInfo.orElseThrow(() -> new ApplicationException("Cannot create route info by id: " +
                    createdRouteInfoId.getId()));
        } else {
            return routeInfo.get();
        }
    }

    private void deleteUnneededRouteInfos(Set<RouteInfo> oldRouteInfos, Set<RouteInfo> newRouteInfo) {
        Optional.ofNullable(oldRouteInfos)
                .orElse(Collections.emptySet()).stream()
                .map(RouteInfo::getId)
                .filter(routeInfoId -> newRouteInfo.stream().noneMatch(routeInfo -> routeInfo.getId().equals(routeInfoId)))
                .forEach(routeInfoService::delete);
    }

    @Override
    public String getServiceName() {
        return "groupRouteInfo";
    }
}
