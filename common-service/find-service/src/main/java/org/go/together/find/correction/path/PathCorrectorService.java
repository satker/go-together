package org.go.together.find.correction.path;

import org.go.together.find.dto.FieldMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PathCorrectorService implements PathCorrector {
    public CorrectedPathDto getCorrectedPath(String[] localPaths, Map<String, FieldMapper> fieldMappers) {
        StringBuilder result = new StringBuilder();
        Map<String, FieldMapper> currentFieldMapper = fieldMappers;
        for (int i = 0; i < localPaths.length - 1; i++) {
            FieldMapper fieldMapper = currentFieldMapper.get(localPaths[i]);
            result.append(fieldMapper.getCurrentServiceField());
            if (fieldMapper.getInnerService() != null) {
                currentFieldMapper = fieldMapper.getInnerService().getMappingFields();
            }
        }
        return CorrectedPathDto.builder()
                .correctedPath(result)
                .currentFieldMapper(currentFieldMapper).build();
    }
}
