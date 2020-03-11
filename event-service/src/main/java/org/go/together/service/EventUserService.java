package org.go.together.service;

import org.go.together.dto.EventUserDto;
import org.go.together.logic.CrudService;
import org.go.together.mapper.EventUserMapper;
import org.go.together.model.EventUser;
import org.go.together.repository.EventUserRepository;
import org.go.together.validation.EventUserValidator;
import org.springframework.stereotype.Service;

import java.util.Collection;
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

    public boolean saveEventUserByEventId(UUID eventId, EventUserDto eventUserDto) {
        return false;
    }

    public boolean deleteEventUserByEventId(UUID eventId, EventUserDto eventUserDto) {
        return false;
    }
}
