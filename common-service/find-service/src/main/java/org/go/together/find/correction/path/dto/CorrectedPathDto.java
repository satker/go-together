package org.go.together.find.correction.path.dto;

import lombok.Builder;
import lombok.Getter;
import org.go.together.find.dto.FieldMapper;

import java.util.Map;

@Builder
@Getter
public class CorrectedPathDto {
    private final Map<String, FieldMapper> currentFieldMapper;
    private final StringBuilder correctedPath;
}
