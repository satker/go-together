package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.Mapper;
import org.go.together.dto.LocationDto;
import org.go.together.dto.RouteInfoDto;
import org.go.together.kafka.producers.CrudProducer;
import org.go.together.model.RouteInfo;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RouteInfoMapper implements Mapper<RouteInfoDto, RouteInfo> {
    private final CrudProducer<LocationDto> locationProducer;

    @Override
    public RouteInfoDto entityToDto(UUID requestId, RouteInfo entity) {
        RouteInfoDto routeInfoDto = new RouteInfoDto();
        routeInfoDto.setId(entity.getId());
        routeInfoDto.setCost(entity.getCost());
        routeInfoDto.setLocation(locationProducer.read(requestId, entity.getLocationId()));
        routeInfoDto.setMovementDate(entity.getMovementDate());
        routeInfoDto.setMovementDuration(entity.getMovementDuration());
        routeInfoDto.setTransportType(entity.getTransportType());
        routeInfoDto.setRouteNumber(entity.getRouteNumber());
        routeInfoDto.setIsEnd(entity.getIsEnd());
        routeInfoDto.setIsStart(entity.getIsStart());
        return routeInfoDto;
    }

    @Override
    public RouteInfo dtoToEntity(RouteInfoDto dto) {
        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setId(dto.getId());
        routeInfo.setCost(dto.getCost());
        routeInfo.setMovementDate(dto.getMovementDate());
        routeInfo.setMovementDuration(dto.getMovementDuration());
        routeInfo.setLocationId(dto.getLocation().getId());
        routeInfo.setTransportType(dto.getTransportType());
        routeInfo.setIsEnd(dto.getIsEnd());
        routeInfo.setIsStart(dto.getIsStart());
        routeInfo.setRouteNumber(dto.getRouteNumber());
        return routeInfo;
    }
}
