package org.go.together.test.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.compare.ComparableDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class JoinTestDto extends ComparableDto {
    private String name;

    @Override
    public String getMainField() {
        return null;
    }
}
