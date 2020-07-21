package org.go.together.service;

import com.google.common.collect.ImmutableMap;
import org.go.together.dto.EventLocationDto;
import org.go.together.dto.IdDto;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.enums.CrudOperation;
import org.go.together.logic.services.CrudService;
import org.go.together.mapper.EventLocationMapper;
import org.go.together.model.EventLocation;
import org.go.together.repository.EventLocationRepository;
import org.go.together.validation.EventLocationValidator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventLocationService extends CrudService<EventLocationDto, EventLocation> {
    private final EventLocationMapper eventLocationMapper;
    private final EventLocationRepository eventLocationRepository;
    private final LocationService locationService;

    public EventLocationService(EventLocationRepository eventLocationRepository,
                                EventLocationMapper eventLocationMapper,
                                EventLocationValidator eventLocationValidator,
                                LocationService locationService) {
        super(eventLocationRepository, eventLocationMapper, eventLocationValidator);
        this.eventLocationMapper = eventLocationMapper;
        this.eventLocationRepository = eventLocationRepository;
        this.locationService = locationService;
    }

    public Set<EventLocationDto> getEventRoute(UUID eventId) {
        return eventLocationRepository.findByEventId(eventId).stream()
                .map(eventLocationMapper::entityToDto)
                .collect(Collectors.toSet());
    }

    public Set<IdDto> saveOrUpdateEventRoutes(Collection<EventLocationDto> eventLocationDtos, UUID eventId) {
        Set<UUID> presentedEventLocations = eventLocationRepository.findByEventId(eventId).stream()
                .map(EventLocation::getId)
                .collect(Collectors.toSet());
        Set<IdDto> result = new HashSet<>();

        presentedEventLocations.stream()
                .filter(eventLocationId -> eventLocationDtos.stream()
                        .noneMatch(eventLocationDto -> eventLocationId.equals(eventLocationDto.getId())))
                .forEach(super::delete);

        eventLocationDtos
                .forEach(eventLocationDtoEntry -> {
                    if (eventLocationDtoEntry.getId() != null
                            && presentedEventLocations.contains(eventLocationDtoEntry.getId())) {
                        result.add(super.update(eventLocationDtoEntry));
                    } else if (eventLocationDtoEntry.getId() == null) {
                        result.add(super.create(eventLocationDtoEntry));
                    }
                });

        return result;
    }

    @Override
    protected EventLocation enrichEntity(EventLocation entity, EventLocationDto dto, CrudOperation crudOperation) {
        if (crudOperation == CrudOperation.UPDATE || crudOperation == CrudOperation.CREATE) {
            if (entity.getLocation().getId() == null) {
                IdDto idDto = locationService.create(dto.getLocation());
                entity.getLocation().setId(idDto.getId());
            }
        }
        return entity;
    }

    @Override
    public String getServiceName() {
        return "eventLocation";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return ImmutableMap.<String, FieldMapper>builder()
                .put("isStart", FieldMapper.builder()
                        .currentServiceField("isStart").build())
                .put("isEnd", FieldMapper.builder()
                        .currentServiceField("isEnd").build())
                .put("latitude,longitude", FieldMapper.builder()
                        .currentServiceField("latitude,longitude").build()).build();
    }
}
