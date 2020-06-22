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
import org.go.together.repository.EventRepository;
import org.go.together.validation.EventValidator;
import org.springframework.stereotype.Service;

import java.util.*;
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

    private void updateEntity(Event entity, EventDto dto, UUID uuid) {
        Set<EventLocationDto> newRoute = dto.getRoute().stream()
                .peek(routeItem -> routeItem.setEventId(uuid))
                .collect(Collectors.toSet());
        Set<IdDto> routes = locationClient.saveOrUpdateEventRoutes(newRoute);
        entity.setRoutes(routes.stream()
                .map(IdDto::getId)
                .collect(Collectors.toSet()));
        EventPhotoDto eventPhotoDto = dto.getEventPhotoDto();
        eventPhotoDto.setEventId(uuid);
        IdDto photoId = contentClient.savePhotosForEvent(eventPhotoDto);
        entity.setEventPhotoId(photoId.getId());
    }

    @Override
    protected void updateEntity(Event entity, EventDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.UPDATE) {
            updateEntity(entity, dto, entity.getId());
        } else if (crudOperation == CrudOperation.CREATE) {
            UUID uuid = UUID.randomUUID();
            entity.setId(uuid);
            entity.setUsers(Collections.emptySet());
            updateEntity(entity, dto, uuid);
        } else if (crudOperation == CrudOperation.DELETE) {
            locationClient.deleteRoutes(entity.getRoutes());
            contentClient.delete(entity.getEventPhotoId());
        }
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
