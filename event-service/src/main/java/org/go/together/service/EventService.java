package org.go.together.service;

import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.dto.EventDto;
import org.go.together.dto.EventLocationDto;
import org.go.together.dto.EventPhotoDto;
import org.go.together.dto.IdDto;
import org.go.together.logic.CrudService;
import org.go.together.mapper.EventMapper;
import org.go.together.model.Event;
import org.go.together.repository.EventRepository;
import org.go.together.validation.EventValidator;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventService extends CrudService<EventDto, Event> {
    private final LocationClient locationClient;
    private final ContentClient contentClient;

    protected EventService(EventRepository eventRepository,
                           EventMapper eventMapper,
                           EventValidator eventValidator,
                           LocationClient locationClient,
                           ContentClient contentClient) {
        super(eventRepository, eventMapper, eventValidator);
        this.locationClient = locationClient;
        this.contentClient = contentClient;
    }

    @Override
    protected void updateEntityForCreate(Event entity, EventDto dto) {
        UUID uuid = UUID.randomUUID();
        entity.setId(uuid);
        updateEntity(entity, dto, uuid);
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
    protected void updateEntityForUpdate(Event entity, EventDto dto) {
        updateEntity(entity, dto, entity.getId());
    }

    @Override
    public void actionsBeforeDelete(Event event) {
        locationClient.deleteRoutes(event.getRoutes());
        contentClient.delete(event.getEventPhotoId());
    }
}
