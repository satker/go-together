package org.go.together.find.correction.field;

import org.go.together.dto.FieldMapper;
import org.go.together.find.correction.field.dto.CorrectedFieldDto;
import org.go.together.find.dto.FieldDto;

import java.util.Map;

public interface FieldCorrector {
    CorrectedFieldDto correct(Map<String, FieldMapper> fieldMappers, FieldDto fieldDto);
}
