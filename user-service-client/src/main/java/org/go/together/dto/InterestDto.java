package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

@EqualsAndHashCode(callSuper = true)
@Data
public class InterestDto extends ComparableDto {
    @ComparingField("name")
    private String name;

    @Override
    public String getMainField() {
        return name;
    }
}
