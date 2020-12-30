package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.Mapper;
import org.go.together.dto.EventDto;
import org.go.together.dto.GroupPhotoDto;
import org.go.together.dto.GroupRouteInfoDto;
import org.go.together.dto.UserDto;
import org.go.together.kafka.producers.CrudProducer;
import org.go.together.model.Event;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventMapper implements Mapper<EventDto, Event> {
    private final CrudProducer<UserDto> usersCrudProducer;
    private final CrudProducer<GroupPhotoDto> groupPhotoProducer;
    private final CrudProducer<GroupRouteInfoDto> routeInfoProducer;

    @Override
    public EventDto entityToDto(UUID requestId, Event entity) {
        EventDto eventDto = new EventDto();
        eventDto.setId(entity.getId());
        eventDto.setAuthor(usersCrudProducer.read(requestId, entity.getAuthorId()));
        eventDto.setDescription(entity.getDescription());
        eventDto.setPeopleCount(entity.getPeopleCount());
        eventDto.setGroupPhoto(groupPhotoProducer.read(requestId, entity.getGroupPhotoId()));
        eventDto.setRouteInfo(routeInfoProducer.read(requestId, entity.getRouteInfoId()));
        eventDto.setName(entity.getName());
        eventDto.setStartDate(entity.getStartDate());
        eventDto.setEndDate(entity.getEndDate());
        return eventDto;
    }

    @Override
    public Event dtoToEntity(EventDto dto) {
        Event event = new Event();
        event.setId(dto.getId());
        event.setAuthorId(dto.getAuthor().getId());
        event.setDescription(dto.getDescription());
        event.setPeopleCount(dto.getPeopleCount());
        event.setName(dto.getName());
        event.setStartDate(dto.getStartDate());
        event.setEndDate(dto.getEndDate());
        return event;
    }
}
