package org.go.together.find.correction.path;

import org.apache.commons.lang3.StringUtils;
import org.go.together.compare.FieldMapper;
import org.go.together.find.correction.path.dto.CorrectedPathDto;
import org.go.together.find.dto.FieldDto;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PathCorrectorService implements PathCorrector {
    public CorrectedPathDto correct(FieldDto fieldDto, Map<String, FieldMapper> fieldMappers) {
        String[] localPaths = fieldDto.getPaths();

        StringBuilder path = new StringBuilder();
        Map<String, FieldMapper> currentFieldMapper = fieldMappers;
        for (int i = 0; i < localPaths.length - 1; i++) {
            FieldMapper fieldMapper = currentFieldMapper.get(localPaths[i]);
            String currentServiceField = fieldMapper.getCurrentServiceField();
            if (StringUtils.isNotBlank(path) && StringUtils.isNotBlank(currentServiceField)) {
                path.append(".");
            }
            path.append(currentServiceField);
            if (fieldMapper.getInnerService() != null) {
                currentFieldMapper = fieldMapper.getInnerService().getMappingFields();
            }
        }
        return CorrectedPathDto.builder()
                .correctedPath(path)
                .currentFieldMapper(currentFieldMapper).build();
    }
}
