package org.go.together.service;

import org.apache.commons.lang3.StringUtils;
import org.go.together.base.impl.CrudServiceImpl;
import org.go.together.client.UserClient;
import org.go.together.dto.EventUserDto;
import org.go.together.enums.NotificationStatus;
import org.go.together.find.dto.FieldMapper;
import org.go.together.model.Event;
import org.go.together.model.EventUser;
import org.go.together.repository.EventRepository;
import org.go.together.repository.EventUserRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventUserService extends CrudServiceImpl<EventUserDto, EventUser> {
    private final UserClient userClient;
    private final EventRepository eventRepository;

    protected EventUserService(UserClient userClient,
                               EventRepository eventRepository) {
        this.userClient = userClient;
        this.eventRepository = eventRepository;
    }

    public Collection<EventUserDto> getEventUsersByEventId(UUID eventId) {
        return mapper.entitiesToDtos(((EventUserRepository) repository).findEventUserByEventId(eventId));
    }

    public boolean deleteEventUserByEventId(EventUserDto eventUserDto) {
        Optional<EventUser> eventUserByUserIdAndEventId =
                ((EventUserRepository) repository).findEventUserByUserIdAndEventId(eventUserDto.getUser().getId(), eventUserDto.getEventId());
        if (eventUserByUserIdAndEventId.isEmpty()) {
            return false;
        }
        super.delete(eventUserByUserIdAndEventId.get().getId());
        return true;
    }

    @Override
    protected String getNotificationMessage(EventUserDto dto, EventUserDto anotherDto, NotificationStatus notificationStatus) {
        String login = userClient.findLoginById(anotherDto.getUser().getId());
        String eventName = eventRepository.findById(anotherDto.getEventId())
                .map(Event::getName)
                .orElse(StringUtils.EMPTY);
        if (notificationStatus == NotificationStatus.CREATED) {
            return "Added user '" + login + "' to event '" + eventName + "'.";
        } else if (notificationStatus == NotificationStatus.UPDATED) {
            return "Changed user '" + login +
                    "' status to " + anotherDto.getUserStatus().getDescription() +
                    " in event '" + eventName + "'.";
        } else if (notificationStatus == NotificationStatus.DELETED) {
            return "Delete user '" + login + "' from event '" + eventName + "'.";
        }
        return super.getNotificationMessage(dto, anotherDto, notificationStatus);
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
