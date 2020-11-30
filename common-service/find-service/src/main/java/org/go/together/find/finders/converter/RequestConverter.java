package org.go.together.find.finders.converter;

import org.go.together.compare.FieldMapper;
import org.go.together.dto.FilterDto;
import org.go.together.dto.FormDto;
import org.go.together.find.dto.ClientLocalFieldObject;
import org.go.together.find.dto.FieldDto;

import java.util.Map;

public interface RequestConverter {
    Map.Entry<ClientLocalFieldObject, FormDto> convert(Map.Entry<FieldDto, FilterDto> entry,
                                                       FieldMapper fieldMapper);
}
