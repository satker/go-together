package org.go.together.test.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
public class ManyJoinDto extends ComparableDto {
    private UUID id;

    @ComparingField("name")
    private String name;

    @ComparingField("number")
    private Long number;

    @Override
    public String getMainField() {
        return name;
    }
}
