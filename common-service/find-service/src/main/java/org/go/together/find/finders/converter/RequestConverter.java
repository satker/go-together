package org.go.together.find.finders.converter;

import org.go.together.compare.FieldMapper;
import org.go.together.dto.FormDto;
import org.go.together.find.dto.ClientLocalFieldObject;
import org.go.together.find.dto.node.FilterNode;

import java.util.Map;

public interface RequestConverter {
    Map.Entry<ClientLocalFieldObject, FormDto> convert(FilterNode entry,
                                                       FieldMapper fieldMapper);
}
