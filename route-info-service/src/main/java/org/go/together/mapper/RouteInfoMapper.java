package org.go.together.mapper;

import org.go.together.base.Mapper;
import org.go.together.dto.RouteInfoDto;
import org.go.together.model.RouteInfo;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RouteInfoMapper implements Mapper<RouteInfoDto, RouteInfo> {
    @Override
    public RouteInfoDto entityToDto(UUID requestId, RouteInfo entity) {
        RouteInfoDto routeInfoDto = new RouteInfoDto();
        routeInfoDto.setId(entity.getId());
        routeInfoDto.setCost(entity.getCost());
        routeInfoDto.setEndLocationId(entity.getEndLocationId());
        routeInfoDto.setMovementDate(entity.getMovementDate());
        routeInfoDto.setMovementDuration(entity.getMovementDuration());
        routeInfoDto.setStartLocationId(entity.getStartLocationId());
        routeInfoDto.setTransportType(entity.getTransportType());
        return routeInfoDto;
    }

    @Override
    public RouteInfo dtoToEntity(RouteInfoDto dto) {
        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setId(dto.getId());
        routeInfo.setCost(dto.getCost());
        routeInfo.setEndLocationId(dto.getEndLocationId());
        routeInfo.setMovementDate(dto.getMovementDate());
        routeInfo.setMovementDuration(dto.getMovementDuration());
        routeInfo.setStartLocationId(dto.getStartLocationId());
        routeInfo.setTransportType(dto.getTransportType());
        return routeInfo;
    }
}
