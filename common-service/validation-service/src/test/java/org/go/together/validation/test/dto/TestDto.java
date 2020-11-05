package org.go.together.validation.test.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.dto.Dto;
import org.go.together.dto.SimpleDto;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class TestDto extends Dto {
    private String name;
    private Long number;
    private Date date;
    private Date startDate;
    private Date endDate;
    private Long startNumber;
    private Long endNumber;
    private Double latitude;
    private Double longitude;
    private SimpleDto simpleDto;
    private Set<UUID> elements;
    private Set<JoinTestDto> joinTestEntities;
    private Set<ManyJoinDto> manyJoinEntities;
}
