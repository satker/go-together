package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.Dto;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
public class EventDto implements Dto {
    private UUID id;
    private String name;
    private int peopleCount;
    private int peopleLike;
    private UserDto author;
    private HousingType housingType;
    private String description;
    private Collection<UserDto> users;
    private EventPhotoDto eventPhotoDto;
    private Collection<EventPaidThingDto> paidThings;
    private Set<EventLocationDto> route;
    private Date startDate;
    private Date endDate;
}
