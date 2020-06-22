package org.go.together.service;

import org.go.together.dto.EventUserDto;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.logic.services.CrudService;
import org.go.together.mapper.EventUserMapper;
import org.go.together.model.EventUser;
import org.go.together.repository.EventUserRepository;
import org.go.together.validation.EventUserValidator;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventUserService extends CrudService<EventUserDto, EventUser> {
    private final EventUserRepository eventUserRepository;
    private final EventUserMapper eventUserMapper;

    protected EventUserService(EventUserRepository eventUserRepository,
                               EventUserMapper eventUserMapper,
                               EventUserValidator eventUserValidator) {
        super(eventUserRepository, eventUserMapper, eventUserValidator);
        this.eventUserRepository = eventUserRepository;
        this.eventUserMapper = eventUserMapper;
    }

    public Collection<EventUserDto> getEventUsersByEventId(UUID eventId) {
        return eventUserMapper.entitiesToDtos(eventUserRepository.findEventUserByEventId(eventId));
    }

    public boolean deleteEventUserByEventId(EventUserDto eventUserDto) {
        Optional<EventUser> eventUserByUserIdAndEventId =
                eventUserRepository.findEventUserByUserIdAndEventId(eventUserDto.getUser().getId(), eventUserDto.getEventId());
        if (eventUserByUserIdAndEventId.isEmpty()) {
            return false;
        }
        super.delete(eventUserByUserIdAndEventId.get().getId());
        return true;
    }

    @Override
    public String getServiceName() {
        return "eventUser";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return null;
    }
}
