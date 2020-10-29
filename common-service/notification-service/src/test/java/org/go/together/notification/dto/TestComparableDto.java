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

@EqualsAndHashCode(callSuper = false)
@Data
@Builder(toBuilder = true)
public class TestComparableDto extends ComparableDto {
    private UUID id;

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


    private Set<AnotherTestDto> testDtos;

    @Override
    public UUID getOwnerId() {
        return id;
    }

    @Override
    public UUID getParentId() {
        return someObject;
    }
}
