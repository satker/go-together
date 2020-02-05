package org.go.together.mapper;

import org.go.together.dto.EventLocationDto;
import org.go.together.interfaces.Mapper;
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
        eventLocationDto.setAddress(eventLocation.getAddress());
        eventLocationDto.setLatitude(eventLocation.getLatitude());
        eventLocationDto.setLongitude(eventLocation.getLongitude());
        eventLocationDto.setLocation(locationMapper.entityToDto(eventLocation.getLocation()));
        eventLocationDto.setEventId(eventLocation.getEventId());
        return eventLocationDto;
    }

    public EventLocation dtoToEntity(EventLocationDto locationDto) {
        EventLocation apartmentLocation = new EventLocation();
        apartmentLocation.setId(locationDto.getId());
        apartmentLocation.setAddress(locationDto.getAddress());
        apartmentLocation.setLatitude(locationDto.getLatitude());
        apartmentLocation.setLongitude(locationDto.getLongitude());
        apartmentLocation.setLocation(locationMapper.dtoToEntity(locationDto.getLocation()));
        apartmentLocation.setEventId(locationDto.getEventId());
        return apartmentLocation;
    }
}
