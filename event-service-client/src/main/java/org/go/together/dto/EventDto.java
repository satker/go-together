package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.Dto;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Data
public class EventDto implements Dto {
    private UUID id;
    private String name;
    private int peopleCount;
    private UserDto author;
    private HousingType housingType;
    private String description;
    private EventPhotoDto eventPhotoDto;
    private Collection<EventPaidThingDto> paidThings;
    private Collection<EventLocationDto> route;
    private Date startDate;
    private Date endDate;
}
