package org.go.together.find.correction.fieldpath;

import org.apache.commons.lang3.StringUtils;
import org.go.together.compare.FieldMapper;
import org.go.together.exceptions.IncorrectDtoException;
import org.go.together.find.correction.field.FieldCorrector;
import org.go.together.find.correction.field.dto.CorrectedFieldDto;
import org.go.together.find.correction.path.PathCorrector;
import org.go.together.find.correction.path.dto.CorrectedPathDto;
import org.go.together.find.dto.FieldDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FieldPathCorrectorService implements FieldPathCorrector {
    private FieldCorrector fieldCorrector;
    private PathCorrector pathCorrector;

    @Autowired
    public void setPathCorrector(PathCorrector pathCorrector) {
        this.pathCorrector = pathCorrector;
    }

    @Autowired
    public void setFieldCorrector(FieldCorrector fieldCorrector) {
        this.fieldCorrector = fieldCorrector;
    }

    public CorrectedFieldDto getCorrectedFieldDto(FieldDto fieldDto,
                                                  Map<String, FieldMapper> fieldMappers) {
        CorrectedPathDto correctedPath = pathCorrector.correct(fieldDto, fieldMappers);
        Map<String, FieldMapper> lastFieldMapper = correctedPath.getCurrentFieldMapper();
        CorrectedFieldDto correctedField = fieldCorrector.correct(lastFieldMapper, fieldDto);
        FieldDto correctedFieldDto = getCorrectedFieldDto(fieldDto,
                correctedPath.getCorrectedPath(),
                correctedField.getCorrectedField());
        FieldMapper endFieldMapper = lastFieldMapper.entrySet().stream()
                .filter(stringFieldMapperEntry -> fieldDto.getLocalField().endsWith(stringFieldMapperEntry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst().orElseThrow(() -> new IncorrectDtoException("Cannot find last field mapper"));
        return correctedField.toBuilder().fieldDto(correctedFieldDto).fieldMapper(endFieldMapper).build();
    }

    private FieldDto getCorrectedFieldDto(FieldDto fieldDto,
                                          StringBuilder resultFilterString,
                                          String correctedFilterFields) {
        if (resultFilterString.length() > 0) {
            resultFilterString.append(".").append(correctedFilterFields);
        } else {
            resultFilterString.append(correctedFilterFields);
        }
        String fieldDtoRemoteField = fieldDto.getRemoteField();
        String remoteField = StringUtils.isNotBlank(fieldDtoRemoteField) ? fieldDtoRemoteField : null;
        return new FieldDto(resultFilterString.toString(), remoteField);
    }
}
