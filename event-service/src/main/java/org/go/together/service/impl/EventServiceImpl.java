package org.go.together.service.impl;

import lombok.RequiredArgsConstructor;
import org.go.together.base.CommonCrudService;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.*;
import org.go.together.enums.CrudOperation;
import org.go.together.enums.RouteInfoServiceInfo;
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
    private final CrudProducer<GroupPhotoDto> groupPhotoCrudProducer;
    private final FindProducer<UserDto> findUserKafkaProducer;
    private final CrudProducer<EventLikeDto> eventLikesCrudProducer;
    private final CrudProducer<GroupRouteInfoDto> routeInfoCrudProducer;

    @Override
    protected Event enrichEntity(Event entity, EventDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.UPDATE) {
            updateContent(entity, dto);
            updateRouteInfo(entity, dto);
        } else if (crudOperation == CrudOperation.CREATE) {
            createContent(entity, dto);
            createEventLikes(entity);
            createRouteInfo(entity, dto);
        } else if (crudOperation == CrudOperation.DELETE) {
            asyncEnricher.add("groupPhotoDeletion", () -> groupPhotoCrudProducer.delete(entity.getGroupPhotoId()));
            asyncEnricher.add("eventLikesDeletion", () -> eventLikesCrudProducer.delete(entity.getId()));
            asyncEnricher.add("routeInfoDeletion", () -> routeInfoCrudProducer.delete(entity.getRouteInfoId()));
        }
        return entity;
    }

    private void createRouteInfo(Event entity, EventDto dto) {
        GroupRouteInfoDto routeInfo = dto.getRouteInfo();
        routeInfo.setGroupId(entity.getId());
        asyncEnricher.add("routeInfoCreate", () ->
                entity.setRouteInfoId(routeInfoCrudProducer.create(routeInfo).getId()));
    }

    private void updateRouteInfo(Event entity, EventDto dto) {
        GroupRouteInfoDto routeInfo = dto.getRouteInfo();
        routeInfo.setGroupId(entity.getId());
        asyncEnricher.add("routeInfoUpdate", () ->
                entity.setRouteInfoId(routeInfoCrudProducer.update(routeInfo).getId()));
    }

    private void createEventLikes(Event entity) {
        EventLikeDto eventLikeDto = new EventLikeDto();
        eventLikeDto.setEventId(entity.getId());
        eventLikeDto.setUsers(Collections.emptySet());
        asyncEnricher.add("eventLikesCreate", () -> eventLikesCrudProducer.create(eventLikeDto));
    }

    private void createContent(Event entity, EventDto dto) {
        GroupPhotoDto groupPhotoDto = dto.getGroupPhoto();
        groupPhotoDto.setGroupId(entity.getId());
        groupPhotoDto.setCategory(PhotoCategory.EVENT);
        asyncEnricher.add("groupPhotoCreate", () ->
                entity.setGroupPhotoId(groupPhotoCrudProducer.create(groupPhotoDto).getId()));
    }

    private void updateContent(Event entity, EventDto dto) {
        GroupPhotoDto groupPhotoDto = dto.getGroupPhoto();
        groupPhotoDto.setGroupId(entity.getId());
        groupPhotoDto.setCategory(PhotoCategory.EVENT);
        asyncEnricher.add("groupPhotoUpdate", () ->
                entity.setGroupPhotoId(groupPhotoCrudProducer.update(groupPhotoDto).getId()));
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
                "routes", FieldMapper.builder()
                        .currentServiceField("id")
                        .remoteServiceClient(routeInfoCrudProducer)
                        .remoteServiceName(RouteInfoServiceInfo.GROUP_ROUTE_INFO)
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
