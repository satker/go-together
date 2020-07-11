package org.go.together.service;

import org.apache.commons.lang3.StringUtils;
import org.go.together.client.UserClient;
import org.go.together.dto.EventUserDto;
import org.go.together.dto.NotificationStatus;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.logic.services.CrudService;
import org.go.together.mapper.EventUserMapper;
import org.go.together.model.Event;
import org.go.together.model.EventUser;
import org.go.together.repository.EventRepository;
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
    private final UserClient userClient;
    private final EventRepository eventRepository;

    protected EventUserService(EventUserRepository eventUserRepository,
                               EventUserMapper eventUserMapper,
                               EventUserValidator eventUserValidator,
                               UserClient userClient,
                               EventRepository eventRepository) {
        super(eventUserRepository, eventUserMapper, eventUserValidator);
        this.eventUserRepository = eventUserRepository;
        this.eventUserMapper = eventUserMapper;
        this.userClient = userClient;
        this.eventRepository = eventRepository;
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
    protected String getNotificationMessage(EventUserDto dto, NotificationStatus notificationStatus) {
        String login = userClient.findLoginById(dto.getUser().getId());
        String eventName = eventRepository.findById(dto.getEventId())
                .map(Event::getName)
                .orElse(StringUtils.EMPTY);
        if (notificationStatus == NotificationStatus.CREATED) {
            return "Added user '" + login + "' to event '" + eventName + "'.";
        } else if (notificationStatus == NotificationStatus.UPDATED) {
            return "Changed user '" + login +
                    "' status to " + dto.getUserStatus().getDescription() +
                    " in event '" + eventName + "'.";
        } else if (notificationStatus == NotificationStatus.DELETED) {
            return "Delete user '" + login + "' from event '" + eventName + "'.";
        }
        return super.getNotificationMessage(dto, notificationStatus);
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
