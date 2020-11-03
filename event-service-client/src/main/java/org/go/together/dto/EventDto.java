package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
public class EventDto extends ComparableDto {
    private UUID id;

    @ComparingField("event name")
    private String name;

    @ComparingField("people count")
    private Integer peopleCount;

    @ComparingField("author")
    private UserDto author;

    @ComparingField("description")
    private String description;

    @ComparingField("event group photo")
    private GroupPhotoDto groupPhoto;

    @ComparingField("routes")
    private GroupLocationDto route;

    @ComparingField("info routes")
    private GroupRouteInfoDto routeInfo;

    @ComparingField("start date")
    private Date startDate;

    @ComparingField("end date")
    private Date endDate;

    @Override
    public UUID getOwnerId() {
        return this.author.getId();
    }

    @Override
    public UUID getParentId() {
        return getId();
    }

    @Override
    public String getMainField() {
        return name;
    }
}
