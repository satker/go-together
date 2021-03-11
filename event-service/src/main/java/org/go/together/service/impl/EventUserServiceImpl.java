package org.go.together.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.go.together.base.CommonCrudService;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.EventUserDto;
import org.go.together.dto.UserDto;
import org.go.together.enums.NotificationStatus;
import org.go.together.kafka.producers.CrudProducer;
import org.go.together.model.Event;
import org.go.together.model.EventUser;
import org.go.together.repository.interfaces.EventRepository;
import org.go.together.repository.interfaces.EventUserRepository;
import org.go.together.service.interfaces.EventUserService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventUserServiceImpl extends CommonCrudService<EventUserDto, EventUser> implements EventUserService {
    private final EventRepository eventRepository;
    private final CrudProducer<UserDto> usersCrudProducer;

    @Override
    public boolean deleteEventUserByEventId(EventUserDto eventUserDto) {
        Optional<EventUser> eventUserByUserIdAndEventId =
                ((EventUserRepository) repository).findEventUserByUserIdAndEventId(eventUserDto.getUser().getId(),
                        eventUserDto.getEventId());
        if (eventUserByUserIdAndEventId.isEmpty()) {
            return false;
        }
        super.delete(eventUserByUserIdAndEventId.get().getId());
        notificationService.removeReceiver(eventUserDto);
        return true;
    }

    @Override
    public String getNotificationMessage(EventUserDto originalDto, EventUserDto changedDto, NotificationStatus notificationStatus) {
        String login = usersCrudProducer.read(changedDto.getUser().getId()).getLogin();
        String eventName = eventRepository.findById(changedDto.getEventId())
                .map(Event::getName)
                .orElse(StringUtils.EMPTY);
        if (notificationStatus == NotificationStatus.CREATED) {
            return "Added user '" + login + "' to event '" + eventName + "'.";
        } else if (notificationStatus == NotificationStatus.UPDATED) {
            return "Changed user '" + login +
                    "' status to " + changedDto.getUserStatus().getDescription() +
                    " in event '" + eventName + "'.";
        } else if (notificationStatus == NotificationStatus.DELETED) {
            return "Delete user '" + login + "' from event '" + eventName + "'.";
        }
        return null;
    }

    @Override
    public String getServiceName() {
        return "eventUsers";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return Map.of("eventId", FieldMapper.builder()
                .currentServiceField("eventId")
                .fieldClass(UUID.class).build());
    }
}
