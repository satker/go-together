package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.Mapper;
import org.go.together.client.LocationClient;
import org.go.together.client.UserClient;
import org.go.together.dto.EventDto;
import org.go.together.dto.GroupPhotoDto;
import org.go.together.kafka.interfaces.producers.CommonCrudProducer;
import org.go.together.model.Event;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventMapper implements Mapper<EventDto, Event> {
    private final UserClient userClient;
    private final LocationClient locationClient;
    private final CommonCrudProducer<GroupPhotoDto> groupPhotoProducer;

    @Override
    public EventDto entityToDto(Event entity) {
        EventDto eventDto = new EventDto();
        eventDto.setId(entity.getId());
        eventDto.setAuthor(userClient.readUser(entity.getAuthorId()));
        eventDto.setDescription(entity.getDescription());
        eventDto.setPeopleCount(entity.getPeopleCount());
        eventDto.setRoute(locationClient.readGroupLocations(entity.getRouteId()));
        eventDto.setGroupPhoto(groupPhotoProducer.read(entity.getId(), entity.getGroupPhotoId()));
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
