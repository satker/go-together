package org.go.together.service;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.client.UserClient;
import org.go.together.dto.*;
import org.go.together.enums.CrudOperation;
import org.go.together.find.dto.FieldMapper;
import org.go.together.model.Event;
import org.go.together.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService extends CrudServiceImpl<EventDto, Event> {
    private final LocationClient locationClient;
    private final ContentClient contentClient;
    private final UserClient userClient;

    protected EventService(LocationClient locationClient,
                           ContentClient contentClient,
                           UserClient userClient) {
        this.locationClient = locationClient;
        this.contentClient = contentClient;
        this.userClient = userClient;
    }

    @Override
    protected Event enrichEntity(Event entity, EventDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.UPDATE) {
            GroupLocationDto locationDto = dto.getRoute();
            locationDto.setGroupId(entity.getId());
            locationDto.setCategory(LocationCategory.EVENT);
            IdDto route = locationClient.updateRoute(locationDto);
            entity.setRouteId(route.getId());

            GroupPhotoDto groupPhotoDto = dto.getGroupPhoto();
            groupPhotoDto.setGroupId(entity.getId());
            groupPhotoDto.setCategory(PhotoCategory.EVENT);
            IdDto photoId = contentClient.updateGroup(groupPhotoDto);
            entity.setGroupPhotoId(photoId.getId());
        } else if (crudOperation == CrudOperation.CREATE) {
            entity.setUsers(Collections.emptySet());

            GroupLocationDto locationDto = dto.getRoute();
            locationDto.setGroupId(entity.getId());
            locationDto.setCategory(LocationCategory.EVENT);
            IdDto route = locationClient.createRoute(dto.getRoute());
            entity.setRouteId(route.getId());

            GroupPhotoDto groupPhotoDto = dto.getGroupPhoto();
            groupPhotoDto.setGroupId(entity.getId());
            groupPhotoDto.setCategory(PhotoCategory.EVENT);
            IdDto photoId = contentClient.createGroup(groupPhotoDto);
            entity.setGroupPhotoId(photoId.getId());

            EventLikeDto eventLikeDto = new EventLikeDto();
            eventLikeDto.setEventId(entity.getId());
            eventLikeDto.setUsers(Collections.emptySet());
            userClient.createEventLike(eventLikeDto);
        } else if (crudOperation == CrudOperation.DELETE) {
            locationClient.deleteRoute(entity.getRouteId());
            contentClient.delete(entity.getGroupPhotoId());
            userClient.deleteEventLike(entity.getId());
        }
        return entity;
    }

    public Set<SimpleDto> autocompleteEvents(String name) {
        Collection<Event> events;
        if (StringUtils.isNotBlank(name)) {
            events = ((EventRepository) repository).findEventsByNameLike(name, 0, 5);
        } else {
            events = repository.createQuery().fetchWithPageable(0, 5);
        }
        return events.stream()
                .map(event -> new SimpleDto(event.getId().toString(), event.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getServiceName() {
        return "event";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return ImmutableMap.<String, FieldMapper>builder()
                .put("name", FieldMapper.builder()
                        .currentServiceField("name")
                        .fieldClass(String.class).build())
                .put("author", FieldMapper.builder()
                        .currentServiceField("authorId")
                        .remoteServiceClient(userClient)
                        .remoteServiceName("user")
                        .remoteServiceFieldGetter("id")
                        .fieldClass(UUID.class).build())
                .put("idEventRoutes", FieldMapper.builder()
                        .currentServiceField("id")
                        .remoteServiceClient(locationClient)
                        .remoteServiceName("groupLocation")
                        .remoteServiceFieldGetter("groupId")
                        .fieldClass(UUID.class).build())
                .put("startDate", FieldMapper.builder()
                        .currentServiceField("startDate")
                        .fieldClass(Date.class).build())
                .put("endDate", FieldMapper.builder()
                        .fieldClass(Date.class)
                        .currentServiceField("endDate").build())
                .build();
    }
}
