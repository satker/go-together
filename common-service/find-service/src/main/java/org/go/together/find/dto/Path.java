package org.go.together.find.dto;

import lombok.Builder;
import lombok.Getter;
import org.go.together.compare.FieldMapper;

@Builder
@Getter
public class Path {
    private final FieldMapper lastFieldMapper;
    private final Field field;
}
