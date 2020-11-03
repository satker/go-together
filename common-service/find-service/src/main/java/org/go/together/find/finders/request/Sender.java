package org.go.together.find.finders.request;

import org.go.together.dto.form.FormDto;
import org.go.together.find.dto.ClientLocalFieldObject;
import org.go.together.find.dto.FieldDto;

import java.util.Collection;
import java.util.Map;

public interface Sender {
    Map<FieldDto, Collection<Object>> send(Map<ClientLocalFieldObject, FormDto> filtersToAnotherServices);
}
