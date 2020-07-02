package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Data
public class EventDto implements ComparableDto {
    private UUID id;
    @ComparingField(value = "event name", isMain = true)
    private String name;
    @ComparingField("people count")
    private Integer peopleCount;
    @ComparingField("author")
    private UserDto author;
    @ComparingField("housing type")
    private HousingType housingType;
    @ComparingField("description")
    private String description;
    @ComparingField("event photo")
    private EventPhotoDto eventPhotoDto;
    @ComparingField("paid things")
    private Collection<EventPaidThingDto> paidThings;
    @ComparingField("routes")
    private Collection<EventLocationDto> route;
    @ComparingField("start date")
    private Date startDate;
    @ComparingField("end date")
    private Date endDate;

    @Override
    public UUID getOwnerId() {
        return this.author.getId();
    }
}
