package org.go.together.find.correction.values;

import org.go.together.find.correction.field.CorrectedFieldDto;

import java.util.Collection;
import java.util.Map;

public interface ValuesCorrector {
    Collection<Map<String, Object>> getCorrectedValues(CorrectedFieldDto correctedFieldDto,
                                                       Collection<Map<String, Object>> filters);
}
