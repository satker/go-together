package org.go.together.service;

import org.go.together.dto.EventLocationDto;
import org.go.together.dto.IdDto;
import org.go.together.logic.CrudService;
import org.go.together.mapper.EventLocationMapper;
import org.go.together.model.EventLocation;
import org.go.together.repository.EventLocationRepository;
import org.go.together.validation.EventLocationValidator;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventLocationService extends CrudService<EventLocationDto, EventLocation> {
    private final EventLocationMapper eventLocationMapper;
    private EventLocationRepository eventLocationRepository;

    public EventLocationService(EventLocationRepository eventLocationRepository,
                                EventLocationMapper eventLocationMapper,
                                EventLocationValidator eventLocationValidator) {
        super(eventLocationRepository, eventLocationMapper, eventLocationValidator);
        this.eventLocationMapper = eventLocationMapper;
        this.eventLocationRepository = eventLocationRepository;
    }

    /*@Override
    public ImmutableMap<String, FunctionToGetValue> getFields() {
        return ImmutableMap.<String, FunctionToGetValue>builder()
                .put("apartment.id", new FunctionToGetValue(String.class, ApartmentLocation::getApartmentId))
                .put("cityId", new FunctionToGetValue(String.class, ApartmentLocation::getCityId))
                .build();
    }

    @Override
    public String getServiceName() {
        return "apartmentLocation";
    }*/

    public Set<EventLocationDto> getEventRoute(UUID eventId) {
        return eventLocationRepository.findByEventId(eventId).stream()
                .map(eventLocationMapper::entityToDto)
                .collect(Collectors.toSet());
    }

    public Set<IdDto> saveEventRoutes(Set<EventLocationDto> eventLocationDtos) {
        return eventLocationDtos.stream()
                .map(super::create)
                .collect(Collectors.toSet());
    }

    public Set<IdDto> updateEventRoutes(Set<EventLocationDto> eventLocationDtos) {
        return eventLocationDtos.stream()
                .map(super::update)
                .collect(Collectors.toSet());
    }

    public boolean deleteByEventId(Set<EventLocationDto> eventLocationDtos) {
        try {
            eventLocationDtos.stream()
                    .map(EventLocationDto::getId)
                    .forEach(super::delete);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
