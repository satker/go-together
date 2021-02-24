package org.go.together.find.correction.values;

import org.go.together.dto.FilterValueDto;
import org.go.together.find.dto.Path;

public interface ValuesCorrector {
    FilterValueDto correct(Path correctedFieldDto,
                           FilterValueDto values);
}
