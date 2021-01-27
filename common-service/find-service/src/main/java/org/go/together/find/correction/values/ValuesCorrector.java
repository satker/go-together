package org.go.together.find.correction.values;

import org.go.together.dto.FilterValueDto;
import org.go.together.find.correction.field.dto.CorrectedFieldDto;

public interface ValuesCorrector {
    FilterValueDto correct(CorrectedFieldDto correctedFieldDto,
                                        FilterValueDto values);
}
