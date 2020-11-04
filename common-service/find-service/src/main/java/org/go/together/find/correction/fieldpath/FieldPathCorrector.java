package org.go.together.find.correction.fieldpath;

import org.go.together.dto.FieldMapper;
import org.go.together.find.correction.field.dto.CorrectedFieldDto;
import org.go.together.find.dto.FieldDto;

import java.util.Map;

public interface FieldPathCorrector {
    CorrectedFieldDto getCorrectedFieldDto(FieldDto fieldDto, Map<String, FieldMapper> fieldMappers);
}
