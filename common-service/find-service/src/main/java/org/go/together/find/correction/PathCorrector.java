package org.go.together.find.correction;

import org.go.together.find.dto.FieldMapper;

import java.util.Map;

public class PathCorrector {
    private Map<String, FieldMapper> currentFieldMapper;

    public Map<String, FieldMapper> getCurrentFieldMapper() {
        return currentFieldMapper;
    }

    public StringBuilder getCorrectedPath(String[] localPaths, Map<String, FieldMapper> fieldMappers) {
        StringBuilder result = new StringBuilder();
        currentFieldMapper = fieldMappers;
        for (int i = 0; i < localPaths.length - 1; i++) {
            FieldMapper fieldMapper = currentFieldMapper.get(localPaths[i]);
            result.append(fieldMapper.getCurrentServiceField());
            if (fieldMapper.getInnerService() != null) {
                currentFieldMapper = fieldMapper.getInnerService().getMappingFields();
            }
        }
        return result;
    }
}
