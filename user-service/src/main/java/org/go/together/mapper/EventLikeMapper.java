package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.CommonMapper;
import org.go.together.base.Mapper;
import org.go.together.dto.EventLikeDto;
import org.go.together.dto.SimpleUserDto;
import org.go.together.model.EventLike;
import org.go.together.model.SystemUser;
import org.go.together.repository.interfaces.EventLikeRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventLikeMapper extends CommonMapper<EventLikeDto, EventLike> {
    private final EventLikeRepository eventLikeRepository;
    private final Mapper<SimpleUserDto, SystemUser> simpleUserMapper;

    @Override
    public EventLikeDto toDto(EventLike entity) {
        EventLikeDto eventLikeDto = new EventLikeDto();
        eventLikeDto.setId(entity.getId());
        eventLikeDto.setEventId(entity.getEventId());
        eventLikeDto.setUsers(simpleUserMapper.entitiesToDtos(entity.getUsers()));
        return eventLikeDto;
    }

    @Override
    public EventLike toEntity(EventLikeDto dto) {
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
