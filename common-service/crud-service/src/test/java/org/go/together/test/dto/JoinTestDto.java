package org.go.together.test.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.ComparableDto;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
public class JoinTestDto extends ComparableDto {
    private UUID id;
    private String name;

    @Override
    public String getMainField() {
        return null;
    }
}
