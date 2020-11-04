package org.go.together.test.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

@EqualsAndHashCode(callSuper = true)
@Data
public class ManyJoinDto extends ComparableDto {
    @ComparingField("name")
    private String name;

    @ComparingField("number")
    private Long number;

    @Override
    public String getMainField() {
        return name;
    }
}
