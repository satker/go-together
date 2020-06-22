package org.go.together.mapper;

import org.go.together.dto.EventLocationDto;
import org.go.together.logic.Mapper;
import org.go.together.model.EventLocation;
import org.springframework.stereotype.Component;

@Component
public class EventLocationMapper implements Mapper<EventLocationDto, EventLocation> {
    private final LocationMapper locationMapper;

    public EventLocationMapper(LocationMapper locationMapper) {
        this.locationMapper = locationMapper;
    }

    public EventLocationDto entityToDto(EventLocation eventLocation) {
        EventLocationDto eventLocationDto = new EventLocationDto();
        eventLocationDto.setId(eventLocation.getId());
        eventLocationDto.setAddress(eventLocation.getAddress());
        eventLocationDto.setLatitude(eventLocation.getLatitude());
        eventLocationDto.setLongitude(eventLocation.getLongitude());
        eventLocationDto.setLocation(locationMapper.entityToDto(eventLocation.getLocation()));
        eventLocationDto.setEventId(eventLocation.getEventId());
        eventLocationDto.setRouteNumber(eventLocation.getRouteNumber());
        eventLocationDto.setIsEnd(eventLocation.getIsEnd());
        eventLocationDto.setIsStart(eventLocation.getIsStart());
        return eventLocationDto;
    }

    public EventLocation dtoToEntity(EventLocationDto locationDto) {
        EventLocation eventLocation = new EventLocation();
        eventLocation.setId(locationDto.getId());
        eventLocation.setAddress(locationDto.getAddress());
        eventLocation.setLatitude(locationDto.getLatitude());
        eventLocation.setLongitude(locationDto.getLongitude());
        eventLocation.setLocation(locationMapper.dtoToEntity(locationDto.getLocation()));
        eventLocation.setEventId(locationDto.getEventId());
        eventLocation.setRouteNumber(locationDto.getRouteNumber());
        eventLocation.setIsEnd(locationDto.getIsEnd());
        eventLocation.setIsStart(locationDto.getIsStart());
        return eventLocation;
    }
}
