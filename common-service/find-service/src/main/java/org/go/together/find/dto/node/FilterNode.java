package org.go.together.find.dto.node;

import lombok.Data;
import org.go.together.dto.FilterValueDto;
import org.go.together.find.dto.Field;

@Data
public class FilterNode implements Node {
    private Field field;
    private FilterValueDto values;
}
