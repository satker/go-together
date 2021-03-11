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
            IdDto updateContent = updateContent(entity, dto);
            entity.setGroupPhotoId(updateContent.getId());

            IdDto groupRouteInfo = updateRouteInfo(entity, dto);
            entity.setRouteInfoId(groupRouteInfo.getId());
        } else if (crudOperation == CrudOperation.CREATE) {
            IdDto photoId = createContent(entity, dto);
            entity.setGroupPhotoId(photoId.getId());

            createEventLikes(entity);

            IdDto groupRouteInfo = createRouteInfo(entity, dto);
            entity.setRouteInfoId(groupRouteInfo.getId());
        } else if (crudOperation == CrudOperation.DELETE) {
            groupPhotoCrudProducer.delete(entity.getGroupPhotoId());
            eventLikesCrudProducer.delete(entity.getId());
            routeInfoCrudProducer.delete(entity.getRouteInfoId());
        }
        return entity;
    }

    private IdDto createRouteInfo(Event entity, EventDto dto) {
        GroupRouteInfoDto routeInfo = dto.getRouteInfo();
        routeInfo.setGroupId(entity.getId());
        return routeInfoCrudProducer.create(routeInfo);
    }

    private IdDto updateRouteInfo(Event entity, EventDto dto) {
        GroupRouteInfoDto routeInfo = dto.getRouteInfo();
        routeInfo.setGroupId(entity.getId());
        return routeInfoCrudProducer.update(routeInfo);
    }

    private void createEventLikes(Event entity) {
        EventLikeDto eventLikeDto = new EventLikeDto();
        eventLikeDto.setEventId(entity.getId());
        eventLikeDto.setUsers(Collections.emptySet());
        eventLikesCrudProducer.create(eventLikeDto);
    }

    private IdDto createContent(Event entity, EventDto dto) {
        GroupPhotoDto groupPhotoDto = dto.getGroupPhoto();
        groupPhotoDto.setGroupId(entity.getId());
        groupPhotoDto.setCategory(PhotoCategory.EVENT);
        return groupPhotoCrudProducer.create(groupPhotoDto);
    }

    private IdDto updateContent(Event entity, EventDto dto) {
        GroupPhotoDto groupPhotoDto = dto.getGroupPhoto();
        groupPhotoDto.setGroupId(entity.getId());
        groupPhotoDto.setCategory(PhotoCategory.EVENT);
        return groupPhotoCrudProducer.update(groupPhotoDto);
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
