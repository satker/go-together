package org.go.together.find.correction.field;

import org.go.together.find.dto.FieldMapper;

import java.util.Map;

public interface FieldCorrector {
    CorrectedFieldDto getCorrectedField(Map<String, FieldMapper> fieldMappers, String filterField);
}
