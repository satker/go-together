package org.go.together.find.correction.path;

import org.go.together.compare.FieldMapper;
import org.go.together.find.dto.Field;
import org.go.together.find.dto.Path;

import java.util.Map;

public interface PathCorrector {
    Path correct(Field field, Map<String, FieldMapper> fieldMappers);
}
