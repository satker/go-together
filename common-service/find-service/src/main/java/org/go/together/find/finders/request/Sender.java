package org.go.together.find.finders.request;

import org.go.together.dto.FormDto;
import org.go.together.find.dto.ClientLocalFieldObject;
import org.go.together.find.dto.FieldDto;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface Sender {
    Map<FieldDto, Collection<Object>> send(UUID requestId, Map<ClientLocalFieldObject, FormDto> filtersToAnotherServices);
}
