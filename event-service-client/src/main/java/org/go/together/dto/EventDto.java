package org.go.together.dto;

import com.google.common.collect.ImmutableMap;
import lombok.Data;
import org.go.together.interfaces.Dto;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Data
public class EventDto implements Dto {
    private UUID id;
    private String name;
    private Integer peopleCount;
    private UserDto author;
    private HousingType housingType;
    private String description;
    private EventPhotoDto eventPhotoDto;
    private Collection<EventPaidThingDto> paidThings;
    private Collection<EventLocationDto> route;
    private Date startDate;
    private Date endDate;

    @Override
    public Map<String, ComparingObject> getComparingMap() {
        return ImmutableMap.<String, ComparingObject>builder()
                .put("event name", ComparingObject.builder().getDtoField(this::getName).isMain(true).build())
                .put("author", ComparingObject.builder().getDtoField(() -> this.getAuthor().getLogin()).build())
                .put("people count", ComparingObject.builder().getDtoField(this::getPeopleCount).build())
                .put("housing type", ComparingObject.builder().getDtoField(this::getHousingType).build())
                .put("description", ComparingObject.builder().getDtoField(this::getDescription).build())
                .put("start date", ComparingObject.builder().getDtoField(this::getStartDate).build())
                .put("end date", ComparingObject.builder().getDtoField(this::getEndDate).build())
                .put("event photo", ComparingObject.builder().getDtoField(this::getEventPhotoDto).build())
                .put("paid things", ComparingObject.builder().getDtoField(this::getPaidThings).build())
                .put("routes", ComparingObject.builder().getDtoField(this::getRoute).build())
                .build();
    }

    @Override
    public UUID getAuthorId() {
        return author.getAuthorId();
    }
}
