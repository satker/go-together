package org.go.together.find.finders.converter;

import org.go.together.find.dto.ClientLocalFieldObject;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.FieldMapper;
import org.go.together.find.dto.form.FilterDto;
import org.go.together.find.dto.form.FormDto;

import java.util.Map;

public interface RequestConverter {
    Map.Entry<ClientLocalFieldObject, FormDto> convert(Map.Entry<FieldDto, FilterDto> entry,
                                                       FieldMapper fieldMapper);
}
