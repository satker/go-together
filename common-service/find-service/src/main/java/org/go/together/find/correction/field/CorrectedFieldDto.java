package org.go.together.find.correction.field;

import lombok.Builder;
import lombok.Getter;
import org.go.together.find.dto.FieldDto;

import java.util.Map;

@Builder(toBuilder = true)
@Getter
public class CorrectedFieldDto {
    private final Map<String, String> oldNewFilterField;
    private final Map<String, Class> oldValueClass;
    private final String correctedField;
    private final FieldDto fieldDto;
}
