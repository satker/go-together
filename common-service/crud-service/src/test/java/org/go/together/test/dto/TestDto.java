package org.go.together.test.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.compare.ComparableDto;
import org.go.together.compare.ComparingField;
import org.go.together.dto.SimpleDto;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class TestDto extends ComparableDto {
    @ComparingField("name")
    private String name;

    @ComparingField("number")
    private Long number;

    @ComparingField(value = "date", deepCompare = false)
    private Date date;

    @ComparingField("start date")
    private Date startDate;

    @ComparingField("end date")
    private Date endDate;

    @ComparingField("start number")
    private Long startNumber;

    @ComparingField("end number")
    private Long endNumber;

    @ComparingField("latitude")
    private Double latitude;

    @ComparingField("longitude")
    private Double longitude;

    @ComparingField("simple dto")
    private SimpleDto simpleDto;

    private Set<UUID> elements;

    @ComparingField("join test entities")
    private Set<JoinTestDto> joinTestEntities;

    @ComparingField("many join entities")
    private Set<ManyJoinDto> manyJoinEntities;

    @Override
    public UUID getOwnerId() {
        return getId();
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
