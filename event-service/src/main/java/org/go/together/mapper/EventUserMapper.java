package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.Mapper;
import org.go.together.dto.EventUserDto;
import org.go.together.dto.SimpleUserDto;
import org.go.together.dto.UserDto;
import org.go.together.kafka.producers.CommonCrudProducer;
import org.go.together.model.EventUser;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventUserMapper implements Mapper<EventUserDto, EventUser> {
    private final CommonCrudProducer<UserDto> usersCrudProducer;

    @Override
    public EventUserDto entityToDto(UUID requestId, EventUser entity) {
        EventUserDto eventUserDto = new EventUserDto();
        eventUserDto.setId(entity.getId());
        eventUserDto.setUser(userIdToSimpleUserDto(requestId, entity.getUserId()));
        eventUserDto.setUserStatus(entity.getUserStatus());
        eventUserDto.setEventId(entity.getEventId());
        return eventUserDto;
    }

    private SimpleUserDto userIdToSimpleUserDto(UUID requestId, UUID userId) {
        UserDto userDto = usersCrudProducer.read(requestId, userId);
        SimpleUserDto simpleUserDto = new SimpleUserDto();
        simpleUserDto.setId(userDto.getId());
        simpleUserDto.setFirstName(userDto.getFirstName());
        simpleUserDto.setLastName(userDto.getLastName());
        simpleUserDto.setLogin(userDto.getLogin());
        simpleUserDto.setUserPhoto(userDto.getGroupPhoto().getPhotos().iterator().next());
        return simpleUserDto;
    }

    @Override
    public EventUser dtoToEntity(EventUserDto dto) {
        EventUser eventUser = new EventUser();
        eventUser.setId(dto.getId());
        eventUser.setUserId(dto.getUser().getId());
        eventUser.setUserStatus(dto.getUserStatus());
        eventUser.setEventId(dto.getEventId());
        return eventUser;
    }
}
