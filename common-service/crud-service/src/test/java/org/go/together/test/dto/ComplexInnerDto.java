package org.go.together.test.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.compare.ComparableDto;

@Data
@EqualsAndHashCode(callSuper = true)
public class ComplexInnerDto extends ComparableDto {
    private String name;

    @Override
    public String getMainField() {
        return name;
    }
}
