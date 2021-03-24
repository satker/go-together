package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.CommonMapper;
import org.go.together.dto.EventDto;
import org.go.together.dto.GroupPhotoDto;
import org.go.together.dto.GroupRouteInfoDto;
import org.go.together.dto.UserDto;
import org.go.together.kafka.producers.CrudProducer;
import org.go.together.model.Event;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventMapper extends CommonMapper<EventDto, Event> {
    private final CrudProducer<UserDto> usersCrudProducer;
    private final CrudProducer<GroupPhotoDto> groupPhotoProducer;
    private final CrudProducer<GroupRouteInfoDto> routeInfoProducer;

    @Override
    public EventDto toDto(Event entity) {
        EventDto eventDto = new EventDto();
        eventDto.setId(entity.getId());
        eventDto.setDescription(entity.getDescription());
        eventDto.setPeopleCount(entity.getPeopleCount());
        eventDto.setName(entity.getName());
        eventDto.setStartDate(entity.getStartDate());
        eventDto.setEndDate(entity.getEndDate());
        asyncMapper.add("eventUserRead", () -> eventDto.setAuthor(usersCrudProducer.read(entity.getAuthorId())));
        asyncMapper.add("eventGroupPhotoRead", () -> eventDto.setGroupPhoto(groupPhotoProducer.read(entity.getGroupPhotoId())));
        asyncMapper.add("eventRouteInfoRead", () -> eventDto.setRouteInfo(routeInfoProducer.read(entity.getRouteInfoId())));
        return eventDto;
    }

    @Override
    public Event toEntity(EventDto dto) {
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
