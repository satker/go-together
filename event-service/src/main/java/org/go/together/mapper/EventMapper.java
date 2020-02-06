package org.go.together.mapper;

import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.client.UserClient;
import org.go.together.dto.EventDto;
import org.go.together.dto.IdDto;
import org.go.together.dto.UserDto;
import org.go.together.interfaces.Mapper;
import org.go.together.model.Event;
import org.go.together.model.EventPaidThing;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

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
        eventDto.setAuthor(userClient.findById(entity.getId()));
        eventDto.setDescription(entity.getDescription());
        eventDto.setHousingType(entity.getHousingType());
        eventDto.setLike(entity.getLike());
        eventDto.setPaidThings(eventPaidThingMapper.entitiesToDtos(entity.getPaidThings()));
        eventDto.setPeopleCount(entity.getPeopleCount());
        eventDto.setRoute(entity.getRoutes().stream()
                .map(locationClient::getRouteById)
                .collect(Collectors.toSet()));
        eventDto.setUsers(entity.getUsers().stream()
                .map(userClient::findById)
                .collect(Collectors.toSet()));
        eventDto.setPhoto(contentClient.getPhotosByEventId(entity.getId()).getPhotos().stream()
                .findFirst()
                .orElse(null));
        return eventDto;
    }

    @Override
    public Event dtoToEntity(EventDto dto) {
        Event event = new Event();
        Set<IdDto> routes = locationClient.saveOrUpdateEventRoutes(dto.getRoute());
        event.setId(dto.getId());
        event.setAuthorId(dto.getAuthor().getId());
        event.setDescription(dto.getDescription());
        event.setHousingType(dto.getHousingType());
        event.setLike(dto.getLike());
        event.setPaidThings((Set<EventPaidThing>) eventPaidThingMapper.dtosToEntities(dto.getPaidThings()));
        event.setPeopleCount(dto.getPeopleCount());
        event.setRoutes(routes.stream()
                .map(IdDto::getId)
                .collect(Collectors.toSet()));
        event.setUsers(dto.getUsers().stream()
                .map(UserDto::getId)
                .collect(Collectors.toSet()));
        return null;
    }
}
