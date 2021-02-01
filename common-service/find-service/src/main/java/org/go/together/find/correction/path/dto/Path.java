package org.go.together.find.correction.path.dto;

import lombok.Builder;
import lombok.Getter;
import org.go.together.compare.FieldMapper;
import org.go.together.find.dto.Field;

@Builder
@Getter
public class Path {
    private final FieldMapper lastFieldMapper;
    private final Field field;
}
