package org.go.together.find.correction.path;

import org.go.together.find.dto.FieldMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PathCorrectorService implements PathCorrector {
    public CorrectedPathDto correct(String[] localPaths, Map<String, FieldMapper> fieldMappers) {
        StringBuilder path = new StringBuilder();
        Map<String, FieldMapper> currentFieldMapper = fieldMappers;
        for (int i = 0; i < localPaths.length - 1; i++) {
            FieldMapper fieldMapper = currentFieldMapper.get(localPaths[i]);
            path.append(fieldMapper.getCurrentServiceField());
            if (fieldMapper.getInnerService() != null) {
                currentFieldMapper = fieldMapper.getInnerService().getMappingFields();
            }
        }
        return CorrectedPathDto.builder()
                .correctedPath(path)
                .currentFieldMapper(currentFieldMapper).build();
    }
}
