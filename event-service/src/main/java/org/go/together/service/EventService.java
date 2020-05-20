package org.go.together.service;

import org.apache.commons.lang3.StringUtils;
import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.dto.*;
import org.go.together.dto.filter.PageDto;
import org.go.together.logic.CrudService;
import org.go.together.mapper.EventMapper;
import org.go.together.model.Event;
import org.go.together.repository.EventRepository;
import org.go.together.validation.EventValidator;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventService extends CrudService<EventDto, Event> {
    private final LocationClient locationClient;
    private final ContentClient contentClient;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    protected EventService(EventRepository eventRepository,
                           EventMapper eventMapper,
                           EventValidator eventValidator,
                           LocationClient locationClient,
                           ContentClient contentClient) {
        super(eventRepository, eventMapper, eventValidator);
        this.locationClient = locationClient;
        this.contentClient = contentClient;
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
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

    public ResponseDto<EventDto> find(PageDto page) {
        Set<EventDto> eventsByPagination = eventRepository.findEventsByPagination(page).stream()
                .map(eventMapper::entityToDto)
                .collect(Collectors.toSet());
        long countEvents = eventRepository.getCountEvents();
        PageDto pageDto = new PageDto();
        pageDto.setPage(page.getPage());
        pageDto.setSize(page.getSize());
        pageDto.setTotalSize(countEvents);
        pageDto.setSort(page.getSort());
        return new ResponseDto<>(pageDto, eventsByPagination);
    }
}
