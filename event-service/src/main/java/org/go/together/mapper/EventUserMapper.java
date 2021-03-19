package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.CommonMapper;
import org.go.together.dto.EventUserDto;
import org.go.together.dto.SimpleUserDto;
import org.go.together.dto.UserDto;
import org.go.together.kafka.producers.CrudProducer;
import org.go.together.model.EventUser;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventUserMapper extends CommonMapper<EventUserDto, EventUser> {
    private final CrudProducer<UserDto> usersCrudProducer;

    @Override
    public EventUserDto toDto(EventUser entity) {
        EventUserDto eventUserDto = new EventUserDto();
        eventUserDto.setId(entity.getId());
        eventUserDto.setUser(userIdToSimpleUserDto(entity.getUserId()));
        eventUserDto.setUserStatus(entity.getUserStatus());
        eventUserDto.setEventId(entity.getEventId());
        return eventUserDto;
    }

    private SimpleUserDto userIdToSimpleUserDto(UUID userId) {
        SimpleUserDto simpleUserDto = new SimpleUserDto();
        UserDto userDto = usersCrudProducer.read(userId);
        simpleUserDto.setId(userDto.getId());
        simpleUserDto.setFirstName(userDto.getFirstName());
        simpleUserDto.setLastName(userDto.getLastName());
        simpleUserDto.setLogin(userDto.getLogin());
        simpleUserDto.setUserPhoto(userDto.getGroupPhoto().getPhotos().iterator().next());
        return simpleUserDto;
    }

    @Override
    public EventUser toEntity(EventUserDto dto) {
        EventUser eventUser = new EventUser();
        eventUser.setId(dto.getId());
        eventUser.setUserId(dto.getUser().getId());
        eventUser.setUserStatus(dto.getUserStatus());
        eventUser.setEventId(dto.getEventId());
        return eventUser;
    }
}
