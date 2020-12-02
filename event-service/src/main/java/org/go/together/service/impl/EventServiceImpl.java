package org.go.together.service.impl;

import lombok.RequiredArgsConstructor;
import org.go.together.base.CommonCrudService;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.*;
import org.go.together.enums.CrudOperation;
import org.go.together.enums.LocationServiceInfo;
import org.go.together.kafka.producers.CrudProducer;
import org.go.together.kafka.producers.FindProducer;
import org.go.together.model.Event;
import org.go.together.service.interfaces.EventService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static org.go.together.enums.UserServiceInfo.USERS;

@Service
@RequiredArgsConstructor
public class EventServiceImpl extends CommonCrudService<EventDto, Event> implements EventService {
    private final CrudProducer<GroupLocationDto> groupLocationCrudProducer;
    private final CrudProducer<GroupPhotoDto> groupPhotoCrudProducer;
    private final FindProducer<GroupLocationDto> findLocationKafkaProducer;
    private final FindProducer<UserDto> findUserKafkaProducer;
    private final CrudProducer<EventLikeDto> eventLikesCrudProducer;
    private final CrudProducer<GroupRouteInfoDto> routeInfoCrudProducer;

    @Override
    protected Event enrichEntity(UUID requestId, Event entity, EventDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.UPDATE) {
            IdDto updateGroupLocations = updateLocations(requestId, entity, dto);
            entity.setRouteId(updateGroupLocations.getId());

            IdDto updateContent = updateContent(requestId, entity, dto);
            entity.setGroupPhotoId(updateContent.getId());

            IdDto groupRouteInfo = updateRouteInfo(requestId, entity, dto);
            entity.setRouteInfoId(groupRouteInfo.getId());
        } else if (crudOperation == CrudOperation.CREATE) {
            IdDto route = createLocations(requestId, entity, dto);
            entity.setRouteId(route.getId());

            IdDto photoId = createContent(requestId, entity, dto);
            entity.setGroupPhotoId(photoId.getId());

            createEventLikes(requestId, entity);

            IdDto groupRouteInfo = createRouteInfo(requestId, entity, dto);
            entity.setRouteInfoId(groupRouteInfo.getId());
        } else if (crudOperation == CrudOperation.DELETE) {
            groupLocationCrudProducer.delete(requestId, entity.getRouteId());
            groupPhotoCrudProducer.delete(requestId, entity.getGroupPhotoId());
            eventLikesCrudProducer.delete(requestId, entity.getId());
            routeInfoCrudProducer.delete(requestId, entity.getRouteInfoId());
        }
        return entity;
    }

    private IdDto createRouteInfo(UUID requestId, Event entity, EventDto dto) {
        GroupRouteInfoDto routeInfo = dto.getRouteInfo();
        routeInfo.setGroupId(entity.getId());
        return routeInfoCrudProducer.create(requestId, routeInfo);
    }

    private IdDto updateRouteInfo(UUID requestId, Event entity, EventDto dto) {
        GroupRouteInfoDto routeInfo = dto.getRouteInfo();
        routeInfo.setGroupId(entity.getId());
        return routeInfoCrudProducer.update(requestId, routeInfo);
    }

    private void createEventLikes(UUID requestId, Event entity) {
        EventLikeDto eventLikeDto = new EventLikeDto();
        eventLikeDto.setEventId(entity.getId());
        eventLikeDto.setUsers(Collections.emptySet());
        eventLikesCrudProducer.create(requestId, eventLikeDto);
    }

    private IdDto createContent(UUID requestId, Event entity, EventDto dto) {
        GroupPhotoDto groupPhotoDto = dto.getGroupPhoto();
        groupPhotoDto.setGroupId(entity.getId());
        groupPhotoDto.setCategory(PhotoCategory.EVENT);
        return groupPhotoCrudProducer.create(requestId, groupPhotoDto);
    }

    private IdDto createLocations(UUID requestId, Event entity, EventDto dto) {
        GroupLocationDto locationDto = dto.getRoute();
        locationDto.setGroupId(entity.getId());
        locationDto.setCategory(LocationCategory.EVENT);
        return groupLocationCrudProducer.create(requestId, dto.getRoute());
    }

    private IdDto updateLocations(UUID requestId, Event entity, EventDto dto) {
        GroupLocationDto locationDto = dto.getRoute();
        locationDto.setGroupId(entity.getId());
        locationDto.setCategory(LocationCategory.EVENT);
        return groupLocationCrudProducer.update(requestId, locationDto);
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
        return Map.of(
                "name", FieldMapper.builder()
                        .currentServiceField("name")
                        .fieldClass(String.class).build(),
                "authorId", FieldMapper.builder()
                        .currentServiceField("authorId")
                        .fieldClass(UUID.class).build(),
                "author", FieldMapper.builder()
                        .currentServiceField("authorId")
                        .remoteServiceClient(findUserKafkaProducer)
                        .remoteServiceName(USERS)
                        .remoteServiceFieldGetter("id")
                        .fieldClass(UUID.class).build(),
                "idEventRoutes", FieldMapper.builder()
                        .currentServiceField("id")
                        .remoteServiceClient(findLocationKafkaProducer)
                        .remoteServiceName(LocationServiceInfo.GROUP_LOCATION)
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
