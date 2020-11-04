package org.go.together.find.correction.path;

import org.go.together.dto.FieldMapper;
import org.go.together.find.correction.path.dto.CorrectedPathDto;
import org.go.together.find.dto.FieldDto;

import java.util.Map;

public interface PathCorrector {
    CorrectedPathDto correct(FieldDto fieldDto, Map<String, FieldMapper> fieldMappers);
}
