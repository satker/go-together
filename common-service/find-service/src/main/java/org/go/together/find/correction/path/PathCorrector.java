package org.go.together.find.correction.path;

import org.go.together.find.dto.FieldMapper;

import java.util.Map;

public interface PathCorrector {
    CorrectedPathDto getCorrectedPath(String[] localPaths, Map<String, FieldMapper> fieldMappers);
}
