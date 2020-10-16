package org.go.together.find.correction.path;

import org.go.together.find.correction.path.dto.CorrectedPathDto;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.FieldMapper;

import java.util.Map;

public interface PathCorrector {
    CorrectedPathDto correct(FieldDto fieldDto, Map<String, FieldMapper> fieldMappers);
}
