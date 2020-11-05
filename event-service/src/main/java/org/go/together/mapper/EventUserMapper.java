package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.Mapper;
import org.go.together.client.UserClient;
import org.go.together.dto.EventUserDto;
import org.go.together.model.EventUser;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class EventUserMapper implements Mapper<EventUserDto, EventUser> {
    private final UserClient userClient;

    @Override
    public EventUserDto entityToDto(EventUser entity) {
        EventUserDto eventUserDto = new EventUserDto();
        eventUserDto.setId(entity.getId());
        eventUserDto.setUser(userClient.findSimpleUserDtosByUserIds(Collections.singleton(entity.getUserId())).iterator().next());
        eventUserDto.setUserStatus(entity.getUserStatus());
        eventUserDto.setEventId(entity.getEventId());
        return eventUserDto;
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
