package org.go.together.service;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.client.UserClient;
import org.go.together.dto.*;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.enums.CrudOperation;
import org.go.together.logic.services.CrudService;
import org.go.together.mapper.EventMapper;
import org.go.together.model.Event;
import org.go.together.model.EventRoute;
import org.go.together.repository.EventRepository;
import org.go.together.validation.EventValidator;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventService extends CrudService<EventDto, Event> {
    private final LocationClient locationClient;
    private final ContentClient contentClient;
    private final EventRepository eventRepository;
    private final UserClient userClient;

    protected EventService(EventRepository eventRepository,
                           EventMapper eventMapper,
                           EventValidator eventValidator,
                           LocationClient locationClient,
                           ContentClient contentClient,
                           UserClient userClient) {
        super(eventRepository, eventMapper, eventValidator);
        this.locationClient = locationClient;
        this.contentClient = contentClient;
        this.eventRepository = eventRepository;
        this.userClient = userClient;
    }

    private Set<EventRoute> getRoutes(Event entity, EventDto dto) {
        Set<EventLocationDto> newRoute = dto.getRoute().stream()
                .peek(routeItem -> routeItem.setEventId(entity.getId()))
                .collect(Collectors.toSet());
        Set<IdDto> routes = locationClient.saveOrUpdateEventRoutes(newRoute);
        return routes.stream()
                .map(IdDto::getId)
                .map(EventRoute::new)
                .collect(Collectors.toSet());
    }

    @Override
    protected Event enrichEntity(Event entity, EventDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.UPDATE) {
            entity.setRoutes(getRoutes(entity, dto));

            GroupPhotoDto groupPhotoDto = dto.getGroupPhoto();
            groupPhotoDto.setGroupId(entity.getId());
            groupPhotoDto.setCategory(PhotoCategory.EVENT);
            IdDto photoId = contentClient.updateGroup(groupPhotoDto);
            entity.setGroupPhotoId(photoId.getId());
        } else if (crudOperation == CrudOperation.CREATE) {
            entity.setUsers(Collections.emptySet());
            entity.setRoutes(getRoutes(entity, dto));

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
            locationClient.deleteRoutes(entity.getRoutes().stream()
                    .map(EventRoute::getRouteId)
                    .collect(Collectors.toSet()));
            contentClient.delete(entity.getGroupPhotoId());
            userClient.deleteEventLike(entity.getId());
        }
        return entity;
    }

    public Set<SimpleDto> autocompleteEvents(String name) {
        Collection<Event> events;
        if (StringUtils.isNotBlank(name)) {
            events = eventRepository.findEventsByNameLike(name, 0, 5);
        } else {
            events = eventRepository.createQuery().fetchWithPageable(0, 5);
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
                        .currentServiceField("name").build())
                .put("author", FieldMapper.builder()
                        .currentServiceField("authorId")
                        .remoteServiceClient(userClient)
                        .remoteServiceName("user")
                        .remoteServiceFieldGetter("id").build())
                .put("idEventRoutes", FieldMapper.builder()
                        .currentServiceField("id")
                        .remoteServiceClient(locationClient)
                        .remoteServiceName("eventLocation")
                        .remoteServiceFieldGetter("eventId").build())
                .put("startDate", FieldMapper.builder()
                        .currentServiceField("startDate").build())
                .put("endDate", FieldMapper.builder()
                        .currentServiceField("endDate").build())
                .build();
    }
}
