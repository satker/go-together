package org.go.together.validation.test.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.dto.Dto;

@EqualsAndHashCode(callSuper = true)
@Data
public class JoinTestDto extends Dto {
    private String name;
}
