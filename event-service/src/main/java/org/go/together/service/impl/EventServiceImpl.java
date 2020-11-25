package org.go.together.service.impl;

import lombok.RequiredArgsConstructor;
import org.go.together.base.CommonCrudService;
import org.go.together.client.LocationClient;
import org.go.together.client.RouteInfoClient;
import org.go.together.client.UserClient;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.*;
import org.go.together.enums.CrudOperation;
import org.go.together.kafka.base.KafkaCrudClient;
import org.go.together.model.Event;
import org.go.together.service.interfaces.EventService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl extends CommonCrudService<EventDto, Event> implements EventService {
    private final LocationClient locationClient;
    private final KafkaCrudClient<GroupPhotoDto> groupPhotoCrudProducer;
    private final UserClient userClient;
    private final RouteInfoClient routeInfoClient;

    @Override
    protected Event enrichEntity(UUID requestId, Event entity, EventDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.UPDATE) {
            IdDto updateGroupLocations = updateLocations(entity, dto);
            entity.setRouteId(updateGroupLocations.getId());

            IdDto updateContent = updateContent(requestId, entity, dto);
            entity.setGroupPhotoId(updateContent.getId());

            IdDto groupRouteInfo = updateRouteInfo(entity, dto);
            entity.setRouteInfoId(groupRouteInfo.getId());
        } else if (crudOperation == CrudOperation.CREATE) {
            IdDto route = createLocations(entity, dto);
            entity.setRouteId(route.getId());

            IdDto photoId = createContent(requestId, entity, dto);
            entity.setGroupPhotoId(photoId.getId());

            createEventLikes(entity);

            IdDto groupRouteInfo = createRouteInfo(entity, dto);
            entity.setRouteInfoId(groupRouteInfo.getId());
        } else if (crudOperation == CrudOperation.DELETE) {
            locationClient.delete("groupLocations", entity.getRouteId());
            groupPhotoCrudProducer.delete(requestId, entity.getGroupPhotoId());
            userClient.deleteEventLike(entity.getId());
        }
        return entity;
    }

    private IdDto createRouteInfo(Event entity, EventDto dto) {
        GroupRouteInfoDto routeInfo = dto.getRouteInfo();
        routeInfo.setGroupId(entity.getId());
        return routeInfoClient.create("groupRouteInfo", routeInfo);
    }

    private IdDto updateRouteInfo(Event entity, EventDto dto) {
        GroupRouteInfoDto routeInfo = dto.getRouteInfo();
        routeInfo.setGroupId(entity.getId());
        return routeInfoClient.update("groupRouteInfo", routeInfo);
    }

    private void createEventLikes(Event entity) {
        EventLikeDto eventLikeDto = new EventLikeDto();
        eventLikeDto.setEventId(entity.getId());
        eventLikeDto.setUsers(Collections.emptySet());
        userClient.create("eventLikes", eventLikeDto);
    }

    private IdDto createContent(UUID requestId, Event entity, EventDto dto) {
        GroupPhotoDto groupPhotoDto = dto.getGroupPhoto();
        groupPhotoDto.setGroupId(entity.getId());
        groupPhotoDto.setCategory(PhotoCategory.EVENT);
        return groupPhotoCrudProducer.create(requestId, groupPhotoDto);
    }

    private IdDto createLocations(Event entity, EventDto dto) {
        GroupLocationDto locationDto = dto.getRoute();
        locationDto.setGroupId(entity.getId());
        locationDto.setCategory(LocationCategory.EVENT);
        return locationClient.create("groupLocations", dto.getRoute());
    }

    private IdDto updateLocations(Event entity, EventDto dto) {
        GroupLocationDto locationDto = dto.getRoute();
        locationDto.setGroupId(entity.getId());
        locationDto.setCategory(LocationCategory.EVENT);
        return locationClient.update("groupLocations", locationDto);
    }

    private IdDto updateContent(UUID requestId, Event entity, EventDto dto) {
        GroupPhotoDto groupPhotoDto = dto.getGroupPhoto();
        groupPhotoDto.setGroupId(entity.getId());
        groupPhotoDto.setCategory(PhotoCategory.EVENT);
        return groupPhotoCrudProducer.update(requestId, groupPhotoDto);
    }

    @Override
    public String getServiceName() {
        return "events";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return Map.of("name", FieldMapper.builder()
                        .currentServiceField("name")
                        .fieldClass(String.class).build(),
                "authorId", FieldMapper.builder()
                        .currentServiceField("authorId")
                        .fieldClass(UUID.class).build(),
                "author", FieldMapper.builder()
                        .currentServiceField("authorId")
                        .remoteServiceClient(userClient)
                        .remoteServiceName("users")
                        .remoteServiceFieldGetter("id")
                        .fieldClass(UUID.class).build(),
                "idEventRoutes", FieldMapper.builder()
                        .currentServiceField("id")
                        .remoteServiceClient(locationClient)
                        .remoteServiceName("groupLocations")
                        .remoteServiceFieldGetter("groupId")
                        .fieldClass(UUID.class).build(),
                "startDate", FieldMapper.builder()
                        .currentServiceField("startDate")
                        .fieldClass(Date.class).build(),
                "endDate", FieldMapper.builder()
                        .fieldClass(Date.class)
                        .currentServiceField("endDate").build());
    }
}
