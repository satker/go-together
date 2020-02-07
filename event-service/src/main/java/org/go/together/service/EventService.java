package org.go.together.service;

import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.dto.EventDto;
import org.go.together.dto.EventPhotoDto;
import org.go.together.dto.IdDto;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.exceptions.ValidationException;
import org.go.together.logic.CrudService;
import org.go.together.mapper.EventMapper;
import org.go.together.model.Event;
import org.go.together.repository.EventRepository;
import org.go.together.validation.EventValidator;
import org.jooq.tools.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventService extends CrudService<EventDto, Event> {
    private final EventValidator eventValidator;
    private final LocationClient locationClient;
    private final ContentClient contentClient;
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;

    protected EventService(EventRepository eventRepository,
                           EventMapper eventMapper,
                           EventValidator eventValidator,
                           LocationClient locationClient,
                           ContentClient contentClient) {
        super(eventRepository, eventMapper, eventValidator);
        this.eventValidator = eventValidator;
        this.locationClient = locationClient;
        this.eventMapper = eventMapper;
        this.contentClient = contentClient;
        this.eventRepository = eventRepository;
    }

    @Override
    public IdDto create(EventDto eventDto) {
        String validate = eventValidator.validateForCreate(eventDto);
        if (StringUtils.isBlank(validate)) {
            UUID uuid = UUID.randomUUID();
            Event event = eventMapper.dtoToEntity(eventDto);
            event.setId(uuid);
            Set<IdDto> routes = locationClient.saveOrUpdateEventRoutes(eventDto.getRoute());
            event.setRoutes(routes.stream()
                    .map(IdDto::getId)
                    .collect(Collectors.toSet()));
            EventPhotoDto eventPhotoDto = eventDto.getEventPhotoDto();
            eventPhotoDto.setEventId(uuid);
            IdDto photoId = contentClient.savePhotosForEvent(eventPhotoDto);
            event.setEventPhotoId(photoId.getId());

            return new IdDto(eventRepository.save(event).getId());
        } else {
            throw new ValidationException(validate);
        }
    }

    @Override
    public IdDto update(EventDto eventDto) {
        return null;
    }

    @Override
    public void delete(UUID eventId) {
        Optional<Event> entityById = eventRepository.findById(eventId);
        if (entityById.isPresent()) {
            Event event = entityById.get();
            locationClient.deleteRoutes(event.getRoutes());
            contentClient.delete(event.getEventPhotoId());
            super.delete(eventId);
        }
        throw new CannotFindEntityException("Cannot find entity by id " + eventId);
    }
}
