package org.go.together.mapper;

import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.client.UserClient;
import org.go.together.dto.EventDto;
import org.go.together.model.Event;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class EventMapper implements Mapper<EventDto, Event> {
    private final UserClient userClient;
    private final LocationClient locationClient;
    private final EventPaidThingMapper eventPaidThingMapper;
    private final ContentClient contentClient;

    public EventMapper(UserClient userClient,
                       LocationClient locationClient,
                       EventPaidThingMapper eventPaidThingMapper,
                       ContentClient contentClient) {
        this.userClient = userClient;
        this.locationClient = locationClient;
        this.eventPaidThingMapper = eventPaidThingMapper;
        this.contentClient = contentClient;
    }

    @Override
    public EventDto entityToDto(Event entity) {
        EventDto eventDto = new EventDto();
        eventDto.setId(entity.getId());
        eventDto.setAuthor(userClient.findById(entity.getAuthorId()));
        eventDto.setDescription(entity.getDescription());
        eventDto.setHousingType(entity.getHousingType());
        eventDto.setPaidThings(eventPaidThingMapper.entitiesToDtos(entity.getPaidThings()));
        eventDto.setPeopleCount(entity.getPeopleCount());
        eventDto.setRoute(locationClient.getRouteById(entity.getRouteId()));
        eventDto.setGroupPhoto(contentClient.readGroupPhotosById(entity.getGroupPhotoId()));
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
        event.setHousingType(dto.getHousingType());
        event.setPaidThings(new HashSet<>(eventPaidThingMapper.dtosToEntities(dto.getPaidThings())));
        event.setPeopleCount(dto.getPeopleCount());
        event.setName(dto.getName());
        event.setStartDate(dto.getStartDate());
        event.setEndDate(dto.getEndDate());
        return event;
    }
}
