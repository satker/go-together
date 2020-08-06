package org.go.together.mapper;

import org.go.together.model.EventLike;
import org.go.together.notification.dto.EventLikeDto;
import org.go.together.notification.repository.EventLikeRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EventLikeMapper implements Mapper<EventLikeDto, EventLike> {
    private final EventLikeRepository eventLikeRepository;
    private final SimpleUserMapper simpleUserMapper;

    public EventLikeMapper(EventLikeRepository eventLikeRepository,
                           SimpleUserMapper simpleUserMapper) {
        this.eventLikeRepository = eventLikeRepository;
        this.simpleUserMapper = simpleUserMapper;
    }

    @Override
    public EventLikeDto entityToDto(EventLike entity) {
        EventLikeDto eventLikeDto = new EventLikeDto();
        eventLikeDto.setId(entity.getId());
        eventLikeDto.setEventId(entity.getEventId());
        eventLikeDto.setUsers(entity.getUsers().stream()
                .map(simpleUserMapper::entityToDto)
                .collect(Collectors.toSet()));
        return eventLikeDto;
    }

    @Override
    public EventLike dtoToEntity(EventLikeDto dto) {
        EventLike eventLike = new EventLike();
        Optional<EventLike> eventLikeOptional = eventLikeRepository.findByEventId(dto.getEventId());
        if (eventLikeOptional.isPresent()) {
            eventLike = eventLikeOptional.get();
        } else {
            eventLike.setId(dto.getId());
            eventLike.setEventId(dto.getEventId());
        }
        eventLike.setUsers(dto.getUsers().stream()
                .map(simpleUserMapper::dtoToEntity)
                .collect(Collectors.toSet()));
        return eventLike;
    }
}
