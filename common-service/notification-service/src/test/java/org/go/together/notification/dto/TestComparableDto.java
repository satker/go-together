package org.go.together.notification.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.dto.SimpleDto;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder(toBuilder = true)
public class TestComparableDto extends ComparableDto {
    @ComparingField("date")
    private Date date;

    @ComparingField("test named enum")
    private TestNamedEnum testNamedEnum;

    @ComparingField("number")
    private Double number;

    @ComparingField("some object")
    private UUID someObject;

    @ComparingField("simpleDto")
    private SimpleDto simpleDto;

    @ComparingField("string")
    private String string;

    @ComparingField(value = "secure string", deepCompare = false)
    private String secureString;

    @ComparingField("another test dto")
    private AnotherTestDto anotherTestDto;

    @ComparingField(value = "another test dto with id compare", idCompare = true)
    private AnotherTestDto anotherTestDtoWithIdCompare;


    @ComparingField("test dtos")
    private Set<AnotherTestDto> testDtos;

    @Override
    public UUID getOwnerId() {
        return super.getId();
    }

    @Override
    public UUID getParentId() {
        return someObject;
    }

    @Override
    public String getMainField() {
        return string;
    }
}
