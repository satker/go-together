package org.go.together.service.impl;

import lombok.RequiredArgsConstructor;
import org.go.together.base.CommonCrudService;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.IdDto;
import org.go.together.dto.LocationDto;
import org.go.together.dto.RouteInfoDto;
import org.go.together.enums.CrudOperation;
import org.go.together.enums.LocationServiceInfo;
import org.go.together.kafka.producers.CrudProducer;
import org.go.together.model.RouteInfo;
import org.go.together.service.interfaces.RouteInfoService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RouteInfoServiceImpl extends CommonCrudService<RouteInfoDto, RouteInfo>
        implements RouteInfoService {
    private final CrudProducer<LocationDto> locationProducer;

    @Override
    protected RouteInfo enrichEntity(UUID requestId,
                                     RouteInfo entity,
                                     RouteInfoDto dto,
                                     CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.CREATE) {
            IdDto locationIdDto = locationProducer.create(requestId, dto.getLocation());
            entity.setLocationId(locationIdDto.getId());
        }
        if (crudOperation == CrudOperation.UPDATE) {
            IdDto locationIdDto = locationProducer.update(requestId, dto.getLocation());
            entity.setLocationId(locationIdDto.getId());
        }
        if (crudOperation == CrudOperation.DELETE) {
            locationProducer.delete(requestId, entity.getLocationId());
        }
        return entity;
    }

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

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return Map.of("isStart", FieldMapper.builder()
                        .currentServiceField("isStart")
                        .fieldClass(Boolean.class).build(),
                "isEnd", FieldMapper.builder()
                        .currentServiceField("isEnd")
                        .fieldClass(Boolean.class).build(),
                "locationRoutes", FieldMapper.builder()
                        .currentServiceField("locationId")
                        .remoteServiceClient(locationProducer)
                        .remoteServiceName(LocationServiceInfo.LOCATION)
                        .remoteServiceFieldGetter("id")
                        .fieldClass(UUID.class).build());
    }
}
