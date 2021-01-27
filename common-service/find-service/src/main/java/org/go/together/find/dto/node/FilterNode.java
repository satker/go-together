package org.go.together.find.dto.node;

import lombok.Data;
import org.go.together.dto.FilterValueDto;
import org.go.together.find.dto.FieldDto;

@Data
public class FilterNode implements Node {
    private FieldDto field;
    private FilterValueDto values;
}
